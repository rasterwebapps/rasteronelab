import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ResultService } from '../../services/result.service';
import { NotificationService } from '@core/services/notification.service';
import { TestResult, ResultStatus, ResultValue, RESULT_STATUS_COLORS } from '../../models/result.model';

@Component({
  selector: 'app-result-detail',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule,
    MatIconModule, MatChipsModule, MatDividerModule,
    MatProgressSpinnerModule, MatTableModule, MatTooltipModule,
    MatDialogModule, MatFormFieldModule, MatInputModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else if (testResult()) {
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-xl font-bold text-gray-800">{{ testResult()!.testName }}</h2>
          <p class="text-gray-500 text-sm">{{ testResult()!.testCode }} — Result Details</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/results">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>

          @if (testResult()!.status === 'PENDING') {
            <a mat-raised-button color="primary" [routerLink]="['/results', testResult()!.id, 'enter']">
              <mat-icon>edit_note</mat-icon> Enter Results
            </a>
          }
          @if (testResult()!.status === 'ENTERED') {
            <button mat-raised-button color="primary" (click)="onValidate()" [disabled]="actionLoading()">
              <mat-icon>check_circle</mat-icon> Validate
            </button>
          }
          @if (testResult()!.status === 'VALIDATED') {
            <button mat-raised-button color="accent" (click)="onAuthorize()" [disabled]="actionLoading()">
              <mat-icon>verified</mat-icon> Authorize
            </button>
          }
          @if (testResult()!.status === 'AUTHORIZED' || testResult()!.status === 'RELEASED') {
            <button mat-stroked-button color="warn" (click)="showAmendForm.set(true)">
              <mat-icon>edit</mat-icon> Amend
            </button>
          }
        </div>
      </div>

      <!-- Result Header -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <mat-card>
          <mat-card-header>
            <mat-card-title>Test Information</mat-card-title>
          </mat-card-header>
          <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
            <div class="text-gray-500 text-sm">Test Code</div>
            <div class="font-medium">{{ testResult()!.testCode }}</div>
            <div class="text-gray-500 text-sm">Test Name</div>
            <div>{{ testResult()!.testName }}</div>
            <div class="text-gray-500 text-sm">Department</div>
            <div>{{ testResult()!.departmentName ?? '-' }}</div>
            <div class="text-gray-500 text-sm">Order ID</div>
            <div class="text-xs font-mono">{{ testResult()!.orderId }}</div>
            <div class="text-gray-500 text-sm">Patient ID</div>
            <div class="text-xs font-mono">{{ testResult()!.patientId }}</div>
            <div class="text-gray-500 text-sm">Sample ID</div>
            <div>{{ testResult()!.sampleId ?? '-' }}</div>
            <div class="text-gray-500 text-sm">Status</div>
            <div>
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getStatusClass(testResult()!.status)">
                {{ testResult()!.status }}
              </span>
              @if (testResult()!.isCritical) {
                <span class="ml-1 px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-700">CRITICAL</span>
              }
              @if (testResult()!.isAmended) {
                <span class="ml-1 px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">AMENDED</span>
              }
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card>
          <mat-card-header>
            <mat-card-title>Timeline</mat-card-title>
          </mat-card-header>
          <mat-card-content class="mt-4">
            <div class="space-y-4">
              <div class="flex items-start gap-3">
                <div class="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center">
                  <mat-icon class="!text-base text-gray-500">add_circle</mat-icon>
                </div>
                <div>
                  <p class="text-sm font-medium">Created</p>
                  <p class="text-xs text-gray-500">{{ testResult()!.createdAt }}</p>
                </div>
              </div>

              @if (testResult()!.enteredAt) {
                <div class="flex items-start gap-3">
                  <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                    <mat-icon class="!text-base text-blue-600">edit_note</mat-icon>
                  </div>
                  <div>
                    <p class="text-sm font-medium">Results Entered</p>
                    <p class="text-xs text-gray-500">{{ testResult()!.enteredAt }} by {{ testResult()!.enteredBy ?? 'Unknown' }}</p>
                  </div>
                </div>
              }

              @if (testResult()!.validatedAt) {
                <div class="flex items-start gap-3">
                  <div class="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
                    <mat-icon class="!text-base text-green-600">check_circle</mat-icon>
                  </div>
                  <div>
                    <p class="text-sm font-medium">Validated</p>
                    <p class="text-xs text-gray-500">{{ testResult()!.validatedAt }} by {{ testResult()!.validatedBy ?? 'Unknown' }}</p>
                  </div>
                </div>
              }

              @if (testResult()!.authorizedAt) {
                <div class="flex items-start gap-3">
                  <div class="w-8 h-8 rounded-full bg-purple-100 flex items-center justify-center">
                    <mat-icon class="!text-base text-purple-600">verified</mat-icon>
                  </div>
                  <div>
                    <p class="text-sm font-medium">Authorized</p>
                    <p class="text-xs text-gray-500">{{ testResult()!.authorizedAt }} by {{ testResult()!.authorizedBy ?? 'Unknown' }}</p>
                  </div>
                </div>
              }

              @if (testResult()!.isAmended) {
                <div class="flex items-start gap-3">
                  <div class="w-8 h-8 rounded-full bg-orange-100 flex items-center justify-center">
                    <mat-icon class="!text-base text-orange-600">edit</mat-icon>
                  </div>
                  <div>
                    <p class="text-sm font-medium">Amended</p>
                    <p class="text-xs text-gray-500">Reason: {{ testResult()!.amendmentReason ?? '-' }}</p>
                  </div>
                </div>
              }
            </div>
          </mat-card-content>
        </mat-card>
      </div>

      <!-- Result Values -->
      <mat-card class="mb-6">
        <mat-card-header>
          <mat-card-title>Result Values</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <table mat-table [dataSource]="sortedValues()" class="w-full">
            <ng-container matColumnDef="parameterName">
              <th mat-header-cell *matHeaderCellDef>Parameter</th>
              <td mat-cell *matCellDef="let rv">
                <div class="font-medium">{{ rv.parameterName }}</div>
                <div class="text-xs text-gray-400">{{ rv.parameterCode }}</div>
              </td>
            </ng-container>

            <ng-container matColumnDef="value">
              <th mat-header-cell *matHeaderCellDef>Value</th>
              <td mat-cell *matCellDef="let rv" class="font-medium"
                [class.text-red-700]="rv.isCritical"
                [class.text-orange-600]="!rv.isCritical && (rv.abnormalFlag === 'HIGH' || rv.abnormalFlag === 'LOW')">
                {{ rv.numericValue ?? rv.textValue ?? '-' }}
              </td>
            </ng-container>

            <ng-container matColumnDef="unit">
              <th mat-header-cell *matHeaderCellDef>Unit</th>
              <td mat-cell *matCellDef="let rv" class="text-gray-500">{{ rv.unit ?? '' }}</td>
            </ng-container>

            <ng-container matColumnDef="referenceRange">
              <th mat-header-cell *matHeaderCellDef>Reference Range</th>
              <td mat-cell *matCellDef="let rv" class="text-gray-500">
                @if (rv.referenceRangeLow != null && rv.referenceRangeHigh != null) {
                  {{ rv.referenceRangeLow }} – {{ rv.referenceRangeHigh }}
                } @else if (rv.referenceRangeText) {
                  {{ rv.referenceRangeText }}
                } @else {
                  -
                }
              </td>
            </ng-container>

            <ng-container matColumnDef="abnormalFlag">
              <th mat-header-cell *matHeaderCellDef>Flag</th>
              <td mat-cell *matCellDef="let rv">
                @if (rv.isCritical) {
                  <span class="px-2 py-1 rounded-full text-xs font-bold bg-red-100 text-red-700">CRITICAL</span>
                } @else if (rv.abnormalFlag === 'HIGH') {
                  <span class="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">HIGH</span>
                } @else if (rv.abnormalFlag === 'LOW') {
                  <span class="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">LOW</span>
                } @else if (rv.abnormalFlag === 'NORMAL') {
                  <span class="px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700">NORMAL</span>
                }
              </td>
            </ng-container>

            <ng-container matColumnDef="previousValue">
              <th mat-header-cell *matHeaderCellDef>Previous</th>
              <td mat-cell *matCellDef="let rv" class="text-gray-500">
                {{ rv.previousValue ?? '-' }}
                @if (rv.deltaPercentage != null) {
                  <span class="text-xs ml-1">(Δ{{ rv.deltaPercentage }}%)</span>
                }
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="valueColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: valueColumns"
              [class.bg-red-50]="row.isCritical"
              [class.bg-orange-50]="!row.isCritical && (row.abnormalFlag === 'HIGH' || row.abnormalFlag === 'LOW')"></tr>
          </table>

          @if (testResult()!.resultValues.length === 0) {
            <div class="text-center py-8 text-gray-400">
              <mat-icon class="!text-5xl mb-2">lab_research</mat-icon>
              <p>No result values recorded yet</p>
            </div>
          }
        </mat-card-content>
      </mat-card>

      <!-- Comments -->
      @if (testResult()!.comments) {
        <mat-card class="mb-6">
          <mat-card-header>
            <mat-card-title>Comments</mat-card-title>
          </mat-card-header>
          <mat-card-content class="mt-4">
            <p class="text-gray-700">{{ testResult()!.comments }}</p>
          </mat-card-content>
        </mat-card>
      }

      <!-- Amend Form -->
      @if (showAmendForm()) {
        <mat-card class="mb-6 border border-orange-200">
          <mat-card-header>
            <mat-card-title>Amend Result</mat-card-title>
          </mat-card-header>
          <mat-card-content class="mt-4">
            <form [formGroup]="amendForm" (ngSubmit)="onAmend()">
              <mat-form-field appearance="outline" class="w-full">
                <mat-label>Amendment Reason</mat-label>
                <textarea matInput formControlName="amendmentReason" rows="3"
                  placeholder="Provide reason for amendment (required)"></textarea>
                @if (amendForm.controls.amendmentReason.hasError('required')) {
                  <mat-error>Amendment reason is required</mat-error>
                }
              </mat-form-field>
              <div class="flex justify-end gap-3">
                <button mat-button type="button" (click)="showAmendForm.set(false)">Cancel</button>
                <button mat-raised-button color="warn" type="submit"
                  [disabled]="amendForm.invalid || actionLoading()">
                  Submit Amendment
                </button>
              </div>
            </form>
          </mat-card-content>
        </mat-card>
      }
    }
  `,
})
export class ResultDetailComponent implements OnInit {
  private readonly resultService = inject(ResultService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly notification = inject(NotificationService);

  readonly testResult = signal<TestResult | null>(null);
  readonly loading = signal(false);
  readonly actionLoading = signal(false);
  readonly showAmendForm = signal(false);

  readonly valueColumns = [
    'parameterName', 'value', 'unit', 'referenceRange',
    'abnormalFlag', 'previousValue',
  ];

  readonly amendForm = this.fb.nonNullable.group({
    amendmentReason: ['', Validators.required],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadResult(id);
    }
  }

  private loadResult(id: string): void {
    this.loading.set(true);
    this.resultService.getById(id).subscribe({
      next: (response) => {
        this.testResult.set(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  sortedValues(): ResultValue[] {
    const result = this.testResult();
    if (!result) return [];
    return [...result.resultValues].sort((a, b) => a.sortOrder - b.sortOrder);
  }

  getStatusClass(status: ResultStatus): string {
    return RESULT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }

  onValidate(): void {
    const result = this.testResult();
    if (!result) return;
    this.actionLoading.set(true);
    this.resultService.validateResult(result.id).subscribe({
      next: (response) => {
        this.testResult.set(response.data);
        this.notification.showSuccess('Result validated successfully');
        this.actionLoading.set(false);
      },
      error: () => {
        this.notification.showError('Failed to validate result');
        this.actionLoading.set(false);
      },
    });
  }

  onAuthorize(): void {
    const result = this.testResult();
    if (!result) return;
    this.actionLoading.set(true);
    this.resultService.authorizeResult(result.id).subscribe({
      next: (response) => {
        this.testResult.set(response.data);
        this.notification.showSuccess('Result authorized successfully');
        this.actionLoading.set(false);
      },
      error: () => {
        this.notification.showError('Failed to authorize result');
        this.actionLoading.set(false);
      },
    });
  }

  onAmend(): void {
    const result = this.testResult();
    if (!result || this.amendForm.invalid) return;
    this.actionLoading.set(true);
    this.resultService.amendResult({
      testResultId: result.id,
      amendmentReason: this.amendForm.getRawValue().amendmentReason,
    }).subscribe({
      next: (response) => {
        this.testResult.set(response.data);
        this.notification.showSuccess('Result amended successfully');
        this.showAmendForm.set(false);
        this.amendForm.reset();
        this.actionLoading.set(false);
      },
      error: () => {
        this.notification.showError('Failed to amend result');
        this.actionLoading.set(false);
      },
    });
  }
}
