import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SampleService } from '../../services/sample.service';
import { SampleTrackingEntry, STATUS_COLORS, SampleStatus } from '../../models/sample.model';

@Component({
  selector: 'app-sample-tracking',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-2xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button [routerLink]="['/samples', sampleId]"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Sample Timeline</h2>
          <p class="text-gray-500 text-sm font-mono">{{ sampleId }}</p>
        </div>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8"><mat-spinner diameter="40" /></div>
      } @else if (events().length === 0) {
        <p class="text-gray-400 text-center py-8">No tracking events found.</p>
      } @else {
        <div class="relative">
          <!-- Vertical line -->
          <div class="absolute left-5 top-0 bottom-0 w-0.5 bg-gray-200"></div>

          @for (event of events(); track event.id; let last = $last) {
            <div class="relative flex gap-4 mb-6">
              <!-- Status dot -->
              <div class="z-10 flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center"
                   [class]="dotClass(event.status)">
                <mat-icon class="text-base leading-none">{{ statusIcon(event.status) }}</mat-icon>
              </div>
              <mat-card class="flex-1">
                <mat-card-content class="pt-3">
                  <div class="flex items-center justify-between">
                    <span class="font-medium px-2 py-0.5 rounded-full text-xs"
                          [class]="statusClass(event.status)">{{ event.status }}</span>
                    <span class="text-xs text-gray-400">{{ fmtDate(event.eventTime) }}</span>
                  </div>
                  @if (event.comments) {
                    <p class="text-sm text-gray-600 mt-2">{{ event.comments }}</p>
                  }
                  @if (event.performedBy) {
                    <p class="text-xs text-gray-400 mt-1">By: {{ event.performedBy }}</p>
                  }
                </mat-card-content>
              </mat-card>
            </div>
          }
        </div>
      }
    </div>
  `,
})
export class SampleTrackingComponent implements OnInit {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);

  events = signal<SampleTrackingEntry[]>([]);
  loading = signal(true);
  sampleId = '';

  ngOnInit(): void {
    this.sampleId = this.route.snapshot.paramMap.get('id')!;
    this.sampleService.getTracking(this.sampleId).subscribe({
      next: res => { this.events.set(res.data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  fmtDate(iso: string): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  statusClass(status: string): string {
    return STATUS_COLORS[status as SampleStatus] ?? 'bg-gray-100 text-gray-700';
  }

  dotClass(status: string): string {
    const map: Record<string, string> = {
      COLLECTED:  'bg-blue-100 text-blue-600',
      RECEIVED:   'bg-cyan-100 text-cyan-600',
      ACCEPTED:   'bg-green-100 text-green-600',
      REJECTED:   'bg-red-100 text-red-600',
      ALIQUOTED:  'bg-purple-100 text-purple-600',
      PROCESSING: 'bg-yellow-100 text-yellow-600',
      COMPLETED:  'bg-emerald-100 text-emerald-600',
      STORED:     'bg-gray-100 text-gray-600',
      DISPOSED:   'bg-slate-100 text-slate-500',
    };
    return map[status] ?? 'bg-gray-100 text-gray-600';
  }

  statusIcon(status: string): string {
    const icons: Record<string, string> = {
      COLLECTED:  'colorize',
      RECEIVED:   'inbox',
      ACCEPTED:   'check_circle',
      REJECTED:   'cancel',
      ALIQUOTED:  'call_split',
      PROCESSING: 'autorenew',
      COMPLETED:  'done_all',
      STORED:     'archive',
      DISPOSED:   'delete',
    };
    return icons[status] ?? 'radio_button_unchecked';
  }
}
