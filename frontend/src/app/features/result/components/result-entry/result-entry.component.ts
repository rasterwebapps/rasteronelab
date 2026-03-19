import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SlicePipe } from '@angular/common';
import { ResultService } from '../../services/result.service';
import { NotificationService } from '@core/services/notification.service';
import { TestResult, ResultEntryRequest, RESULT_STATUS_COLORS } from '../../models/result.model';

@Component({
  selector: 'app-result-entry',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatCardModule, MatChipsModule,
    MatDividerModule, MatProgressSpinnerModule, MatTooltipModule, SlicePipe,
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
          <h2 class="text-xl font-bold text-gray-800">Enter Results</h2>
          <p class="text-gray-500 text-sm">{{ testResult()!.testCode }} — {{ testResult()!.testName }}</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/results">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>
        </div>
      </div>

      <mat-card class="mb-4">
        <mat-card-content class="grid grid-cols-2 md:grid-cols-4 gap-4 py-2">
          <div>
            <span class="text-gray-500 text-xs">Test</span>
            <p class="font-medium">{{ testResult()!.testName }}</p>
          </div>
          <div>
            <span class="text-gray-500 text-xs">Patient ID</span>
            <p class="font-medium">{{ testResult()!.patientId | slice:0:8 }}…</p>
          </div>
          <div>
            <span class="text-gray-500 text-xs">Department</span>
            <p class="font-medium">{{ testResult()!.departmentName ?? '-' }}</p>
          </div>
          <div>
            <span class="text-gray-500 text-xs">Status</span>
            <span class="px-2 py-1 rounded-full text-xs font-medium"
              [class]="getStatusClass(testResult()!.status)">
              {{ testResult()!.status }}
            </span>
          </div>
        </mat-card-content>
      </mat-card>

      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-card>
          <mat-card-header>
            <mat-card-title>Parameter Values</mat-card-title>
          </mat-card-header>
          <mat-card-content class="mt-4">
            <div class="overflow-x-auto">
              <table class="w-full text-sm">
                <thead>
                  <tr class="border-b text-left text-gray-500">
                    <th class="p-2 font-medium">Parameter</th>
                    <th class="p-2 font-medium">Value</th>
                    <th class="p-2 font-medium">Unit</th>
                    <th class="p-2 font-medium">Reference Range</th>
                    <th class="p-2 font-medium">Flag</th>
                  </tr>
                </thead>
                <tbody formArrayName="values">
                  @for (param of valuesArray.controls; track param; let i = $index) {
                    <tr class="border-b hover:bg-gray-50" [formGroupName]="i"
                      [class.bg-red-50]="getAbnormalFlag(i) === 'CRITICAL'"
                      [class.bg-orange-50]="getAbnormalFlag(i) === 'HIGH' || getAbnormalFlag(i) === 'LOW'">
                      <td class="p-2">
                        <div class="font-medium">{{ param.get('parameterName')?.value }}</div>
                        <div class="text-xs text-gray-400">{{ param.get('parameterCode')?.value }}</div>
                      </td>
                      <td class="p-2 w-40">
                        @if (param.get('resultType')?.value === 'NUMERIC') {
                          <mat-form-field appearance="outline" class="w-full dense-field">
                            <input matInput formControlName="numericValue" type="number" step="any" />
                          </mat-form-field>
                        } @else {
                          <mat-form-field appearance="outline" class="w-full dense-field">
                            <input matInput formControlName="textValue" />
                          </mat-form-field>
                        }
                      </td>
                      <td class="p-2 text-gray-500">{{ param.get('unit')?.value ?? '' }}</td>
                      <td class="p-2 text-gray-500 text-xs">
                        @if (param.get('referenceRangeLow')?.value != null && param.get('referenceRangeHigh')?.value != null) {
                          {{ param.get('referenceRangeLow')?.value }} – {{ param.get('referenceRangeHigh')?.value }}
                        } @else if (param.get('referenceRangeText')?.value) {
                          {{ param.get('referenceRangeText')?.value }}
                        } @else {
                          -
                        }
                      </td>
                      <td class="p-2">
                        @switch (getAbnormalFlag(i)) {
                          @case ('CRITICAL') {
                            <span class="px-2 py-1 rounded-full text-xs font-bold bg-red-100 text-red-700">CRITICAL</span>
                          }
                          @case ('HIGH') {
                            <span class="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">HIGH</span>
                          }
                          @case ('LOW') {
                            <span class="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-700">LOW</span>
                          }
                          @case ('NORMAL') {
                            <span class="px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700">NORMAL</span>
                          }
                        }
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>

            <mat-divider class="my-4" />

            <mat-form-field appearance="outline" class="w-full">
              <mat-label>Comments</mat-label>
              <textarea matInput formControlName="comments" rows="3"
                placeholder="Add any relevant comments..."></textarea>
            </mat-form-field>
          </mat-card-content>
        </mat-card>

        <div class="flex justify-end gap-3 mt-4">
          <a mat-button routerLink="/results">Cancel</a>
          <button mat-raised-button color="primary" type="submit" [disabled]="saving()">
            @if (saving()) {
              <mat-icon class="animate-spin">refresh</mat-icon>
            }
            Save Results
          </button>
        </div>
      </form>
    }
  `,
  styles: [`
    .dense-field { font-size: 13px; }
    .dense-field .mat-mdc-form-field-infix { padding-top: 8px; padding-bottom: 8px; min-height: 36px; }
  `],
})
export class ResultEntryComponent implements OnInit {
  private readonly resultService = inject(ResultService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly notification = inject(NotificationService);

  readonly testResult = signal<TestResult | null>(null);
  readonly loading = signal(false);
  readonly saving = signal(false);

  readonly form = this.fb.group({
    values: this.fb.array<FormGroup>([]),
    comments: [''],
  });

  get valuesArray(): FormArray<FormGroup> {
    return this.form.controls.values;
  }

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
        this.buildForm(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  private buildForm(result: TestResult): void {
    this.valuesArray.clear();
    const sorted = [...result.resultValues].sort((a, b) => a.sortOrder - b.sortOrder);
    for (const rv of sorted) {
      this.valuesArray.push(this.fb.group({
        parameterId: [rv.parameterId],
        parameterCode: [rv.parameterCode],
        parameterName: [rv.parameterName],
        resultType: [rv.resultType],
        numericValue: [rv.numericValue != null ? String(rv.numericValue) : '', rv.resultType === 'NUMERIC' ? [Validators.required] : []],
        textValue: [rv.textValue ?? '', rv.resultType !== 'NUMERIC' ? [Validators.required] : []],
        unit: [rv.unit],
        referenceRangeLow: [rv.referenceRangeLow],
        referenceRangeHigh: [rv.referenceRangeHigh],
        referenceRangeText: [rv.referenceRangeText],
      }));
    }
    this.form.patchValue({ comments: result.comments ?? '' });
  }

  getAbnormalFlag(index: number): string {
    const group = this.valuesArray.at(index);
    const resultType = group.get('resultType')?.value;
    if (resultType !== 'NUMERIC') return '';

    const value = parseFloat(group.get('numericValue')?.value);
    if (isNaN(value)) return '';

    const low = group.get('referenceRangeLow')?.value;
    const high = group.get('referenceRangeHigh')?.value;
    if (low == null || high == null) return '';

    const lowNum = Number(low);
    const highNum = Number(high);

    if (value < lowNum * 0.5 || value > highNum * 2) return 'CRITICAL';
    if (value < lowNum) return 'LOW';
    if (value > highNum) return 'HIGH';
    return 'NORMAL';
  }

  getStatusClass(status: string): string {
    return RESULT_STATUS_COLORS[status as keyof typeof RESULT_STATUS_COLORS] ?? 'bg-gray-100 text-gray-700';
  }

  onSubmit(): void {
    if (this.form.invalid || !this.testResult()) return;
    this.saving.set(true);

    const raw = this.form.getRawValue();
    const request: ResultEntryRequest = {
      testResultId: this.testResult()!.id,
      values: raw.values.map(v => ({
        parameterId: v['parameterId'] ?? '',
        parameterCode: v['parameterCode'] ?? undefined,
        parameterName: v['parameterName'] ?? undefined,
        resultType: v['resultType'] ?? undefined,
        numericValue: v['numericValue'] || undefined,
        textValue: v['textValue'] || undefined,
        unit: v['unit'] ?? undefined,
        referenceRangeLow: v['referenceRangeLow'] != null ? String(v['referenceRangeLow']) : undefined,
        referenceRangeHigh: v['referenceRangeHigh'] != null ? String(v['referenceRangeHigh']) : undefined,
        referenceRangeText: v['referenceRangeText'] ?? undefined,
      })),
      comments: raw.comments || undefined,
    };

    this.resultService.enterResults(request).subscribe({
      next: () => {
        this.notification.showSuccess('Results entered successfully');
        this.router.navigate(['/results', this.testResult()!.id]);
        this.saving.set(false);
      },
      error: () => {
        this.notification.showError('Failed to save results');
        this.saving.set(false);
      },
    });
  }
}
