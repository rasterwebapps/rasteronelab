import { Injectable, signal } from '@angular/core';

/**
 * LIS-066 — Barcode Scanner Service
 * Integrates USB barcode scanners via two modes:
 * 1. Keyboard wedge: scanner simulates keyboard input — listen to rapid keystrokes ending in Enter
 * 2. WebHID API: native USB HID device communication (where browser supports it)
 *
 * Usage: inject into any component, subscribe to scanResult signal.
 */
@Injectable({ providedIn: 'root' })
export class BarcodeScannerService {
  /** Latest scanned barcode value (null when idle) */
  readonly scanResult = signal<string | null>(null);

  /** True while a HID device is connected */
  readonly deviceConnected = signal(false);

  private keyBuffer = '';
  private keyTimer: ReturnType<typeof setTimeout> | null = null;
  private readonly KEY_TIMEOUT_MS = 50;
  private readonly MIN_BARCODE_LEN = 4;

  private boundKeyHandler: (e: KeyboardEvent) => void;

  constructor() {
    this.boundKeyHandler = this.handleKeyboardWedge.bind(this);
  }

  /**
   * Start listening for keyboard-wedge input on a target element (or document).
   * Call this when the scan input field is active.
   */
  startKeyboardWedge(target: EventTarget = document): void {
    target.addEventListener('keydown', this.boundKeyHandler as EventListener);
  }

  /** Stop keyboard-wedge listener. */
  stopKeyboardWedge(target: EventTarget = document): void {
    target.removeEventListener('keydown', this.boundKeyHandler as EventListener);
    this.clearBuffer();
  }

  /** Manually push a barcode value (e.g., from a text input on Enter). */
  submitBarcode(value: string): void {
    const trimmed = value.trim();
    if (trimmed.length >= this.MIN_BARCODE_LEN) {
      this.scanResult.set(trimmed);
    }
  }

  /** Clear the current scan result. */
  clearScanResult(): void {
    this.scanResult.set(null);
  }

  /**
   * Attempt to connect a USB barcode scanner via WebHID API.
   * Falls back gracefully if WebHID is not available.
   */
  async connectHidScanner(): Promise<void> {
    if (!('hid' in navigator)) {
      console.warn('WebHID API not available in this browser. Using keyboard wedge mode.');
      return;
    }
    try {
      const hidNavigator = navigator as Navigator & { hid: HID };
      const devices = await hidNavigator.hid.requestDevice({ filters: [] });
      if (devices && devices.length > 0) {
        this.deviceConnected.set(true);
        console.log('Barcode scanner connected via WebHID');
      }
    } catch (err) {
      console.warn('WebHID connection cancelled or failed:', err);
    }
  }

  // ── Private ─────────────────────────────────────────────────────────────────

  private handleKeyboardWedge(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      if (this.keyBuffer.length >= this.MIN_BARCODE_LEN) {
        this.scanResult.set(this.keyBuffer);
      }
      this.clearBuffer();
      return;
    }

    // Only accumulate printable characters
    if (event.key.length === 1) {
      this.keyBuffer += event.key;
    }

    // Reset accumulation timer — rapid key presses = scanner input
    if (this.keyTimer) clearTimeout(this.keyTimer);
    this.keyTimer = setTimeout(() => this.clearBuffer(), this.KEY_TIMEOUT_MS);
  }

  private clearBuffer(): void {
    this.keyBuffer = '';
    if (this.keyTimer) {
      clearTimeout(this.keyTimer);
      this.keyTimer = null;
    }
  }
}

// Type shims for WebHID API (not yet in TypeScript lib)
interface HIDDevice {
  readonly productName: string;
}

interface HID {
  requestDevice(options: { filters: unknown[] }): Promise<HIDDevice[]>;
}
