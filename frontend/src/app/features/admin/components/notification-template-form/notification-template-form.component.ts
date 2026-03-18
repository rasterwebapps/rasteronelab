import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { NotificationTemplate } from '../../models/admin.models';

@Component({
  selector: 'app-notification-template-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatSlideToggleModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Notification Template</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update template details' : 'Create a new notification template' }}</p>
      </div>
      <a mat-button routerLink="/admin/notification-templates">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Template Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Template Code</mat-label>
            <input matInput formControlName="templateCode" placeholder="e.g. REPORT_READY_SMS" />
            @if (form.controls.templateCode.hasError('required')) {
              <mat-error>Template code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Template Name</mat-label>
            <input matInput formControlName="templateName" />
            @if (form.controls.templateName.hasError('required')) {
              <mat-error>Template name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Channel</mat-label>
            <mat-select formControlName="channel">
              <mat-option value="SMS">SMS</mat-option>
              <mat-option value="EMAIL">Email</mat-option>
              <mat-option value="WHATSAPP">WhatsApp</mat-option>
            </mat-select>
            @if (form.controls.channel.hasError('required')) {
              <mat-error>Channel is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Event Trigger</mat-label>
            <input matInput formControlName="eventTrigger" placeholder="e.g. REPORT_PUBLISHED" />
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Template Body</mat-label>
            <textarea matInput formControlName="templateBody" rows="6"
              placeholder="Use variables for dynamic content"></textarea>
            @if (form.controls.templateBody.hasError('required')) {
              <mat-error>Template body is required</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Status</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/notification-templates">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Template
        </button>
      </div>
    </form>
  `,
})
export class NotificationTemplateFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    templateCode: ['', Validators.required],
    templateName: ['', Validators.required],
    channel: ['SMS' as string, Validators.required],
    templateBody: ['', Validators.required],
    eventTrigger: [''],
    isActive: [true],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.itemId = id;
      this.loadItem(id);
    }
  }

  private loadItem(id: string): void {
    this.api.get<NotificationTemplate>('config/notification-templates', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          templateCode: item.templateCode,
          templateName: item.templateName,
          channel: item.channel,
          templateBody: item.templateBody,
          eventTrigger: item.eventTrigger,
          isActive: item.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<NotificationTemplate>;

    const request$ = this.isEditMode()
      ? this.api.update<NotificationTemplate>('config/notification-templates', this.itemId, value)
      : this.api.create<NotificationTemplate>('config/notification-templates', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Template ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/notification-templates']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
