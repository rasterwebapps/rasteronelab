import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { BranchService, Branch } from '@core/services/branch.service';

@Component({
  selector: 'app-branch-selector',
  standalone: true,
  imports: [MatSelectModule, MatFormFieldModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline" class="branch-select">
      <mat-label>Branch</mat-label>
      <mat-select
        [value]="branchService.currentBranch()?.id"
        (selectionChange)="onBranchChange($event.value)"
      >
        @for (branch of branchService.availableBranches(); track branch.id) {
          <mat-option [value]="branch.id">
            {{ branch.name }} — {{ branch.city }}
          </mat-option>
        }
      </mat-select>
      <mat-icon matPrefix>business</mat-icon>
    </mat-form-field>
  `,
  styles: `
    .branch-select {
      font-size: 14px;
    }
    :host ::ng-deep .branch-select .mat-mdc-form-field-subscript-wrapper {
      display: none;
    }
  `,
})
export class BranchSelectorComponent {
  readonly branchService = inject(BranchService);

  onBranchChange(branchId: string): void {
    const branch = this.branchService.availableBranches().find(
      (b: Branch) => b.id === branchId
    );
    if (branch) {
      this.branchService.selectBranch(branch);
    }
  }
}
