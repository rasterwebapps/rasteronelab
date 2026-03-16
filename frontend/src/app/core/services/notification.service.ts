import { Injectable, signal } from '@angular/core';

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
}

/**
 * In-app notification service for showing snackbar-style messages.
 * Used by ErrorInterceptor and components to show feedback.
 */
@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly _notifications = signal<Notification[]>([]);
  readonly notifications = this._notifications.asReadonly();

  showSuccess(message: string, duration = 3000): void {
    this.addNotification({ type: 'success', message, duration });
  }

  showError(message: string, duration = 5000): void {
    this.addNotification({ type: 'error', message, duration });
  }

  showWarning(message: string, duration = 4000): void {
    this.addNotification({ type: 'warning', message, duration });
  }

  showInfo(message: string, duration = 3000): void {
    this.addNotification({ type: 'info', message, duration });
  }

  private addNotification(notification: Omit<Notification, 'id'>): void {
    const id = crypto.randomUUID();
    const full: Notification = { ...notification, id };
    this._notifications.update(n => [...n, full]);

    if (notification.duration) {
      setTimeout(() => this.dismiss(id), notification.duration);
    }
  }

  dismiss(id: string): void {
    this._notifications.update(n => n.filter(notif => notif.id !== id));
  }
}
