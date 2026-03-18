import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormArray, FormGroup } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { WorkingHours } from '../../models/admin.models';

const DAYS_OF_WEEK = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

@Component({
  selector: 'app-working-hours-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatCardModule, MatSlideToggleModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Edit Working Hours</h2>
        <p class="text-gray-500 text-sm">Configure opening and closing times for each day</p>
      </div>
      <a mat-button routerLink="/admin/working-hours">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Weekly Schedule</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <div formArrayName="days" class="grid gap-4">
            @for (day of daysArray.controls; track $index; let i = $index) {
              <div [formGroupName]="i"
                class="grid grid-cols-1 md:grid-cols-4 gap-4 items-center p-4 rounded-lg border">
                <div class="font-medium text-gray-700">{{ formatDay(DAYS[i]) }}</div>
                <mat-form-field appearance="outline">
                  <mat-label>Open Time</mat-label>
                  <input matInput type="time" formControlName="openTime" />
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Close Time</mat-label>
                  <input matInput type="time" formControlName="closeTime" />
                </mat-form-field>
                <mat-slide-toggle formControlName="isClosed">Closed</mat-slide-toggle>
              </div>
            }
          </div>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/working-hours">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          Save Working Hours
        </button>
      </div>
    </form>
  `,
})
export class WorkingHoursFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly saving = signal(false);
  readonly DAYS = DAYS_OF_WEEK;

  readonly form = this.fb.group({
    days: this.fb.array(
      DAYS_OF_WEEK.map(day =>
        this.fb.group({
          dayOfWeek: [day],
          openTime: ['09:00'],
          closeTime: ['18:00'],
          isClosed: [false],
        })
      )
    ),
  });

  get daysArray(): FormArray<FormGroup> {
    return this.form.controls.days as FormArray<FormGroup>;
  }

  ngOnInit(): void {
    this.loadItems();
  }

  private loadItems(): void {
    this.api.list<WorkingHours>('config/working-hours', { page: 0, size: 7 }).subscribe({
      next: (response) => {
        if (response.data.length > 0) {
          response.data.forEach(wh => {
            const index = DAYS_OF_WEEK.indexOf(wh.dayOfWeek);
            if (index >= 0) {
              this.daysArray.at(index).patchValue({
                openTime: wh.openTime,
                closeTime: wh.closeTime,
                isClosed: wh.isClosed,
              });
            }
          });
        }
      },
    });
  }

  onSubmit(): void {
    this.saving.set(true);
    const value = this.daysArray.getRawValue();

    this.api.update<WorkingHours[]>('config/working-hours', '', value as unknown as Partial<WorkingHours[]>).subscribe({
      next: () => {
        this.notification.showSuccess('Working hours saved successfully');
        this.router.navigate(['/admin/working-hours']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }

  formatDay(day: string): string {
    return day.charAt(0) + day.slice(1).toLowerCase();
  }
}
