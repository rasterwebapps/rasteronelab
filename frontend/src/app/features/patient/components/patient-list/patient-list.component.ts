import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PatientService } from '../../services/patient.service';
import { Patient } from '../../models/patient.model';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Patient Registry</h2>
        <p class="text-gray-500 text-sm">Manage patient records and demographics</p>
      </div>
      <a mat-raised-button color="primary" routerLink="register">
        <mat-icon>person_add</mat-icon> Register Patient
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search patients</mat-label>
          <input matInput placeholder="Search by UHID, name, or phone..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="patients()" class="w-full">
          <ng-container matColumnDef="uhid">
            <th mat-header-cell *matHeaderCellDef>UHID</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.uhid }}</td>
          </ng-container>
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Name</th>
            <td mat-cell *matCellDef="let row">{{ row.firstName }} {{ row.lastName }}</td>
          </ng-container>
          <ng-container matColumnDef="ageGender">
            <th mat-header-cell *matHeaderCellDef>Age / Gender</th>
            <td mat-cell *matCellDef="let row">
              {{ formatAge(row) }} / {{ formatGender(row.gender) }}
            </td>
          </ng-container>
          <ng-container matColumnDef="phone">
            <th mat-header-cell *matHeaderCellDef>Phone</th>
            <td mat-cell *matCellDef="let row">{{ row.phone }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id]" matTooltip="View">
                <mat-icon>visibility</mat-icon>
              </a>
              <a mat-icon-button [routerLink]="[row.id, 'edit']" matTooltip="Edit">
                <mat-icon>edit</mat-icon>
              </a>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (patients().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">person_search</mat-icon>
            <p>No patients found</p>
          </div>
        }
      }

      <mat-paginator
        [length]="totalElements()"
        [pageSize]="pageSize()"
        [pageSizeOptions]="[10, 25, 50]"
        (page)="onPage($event)"
        showFirstLastButtons
      />
    </div>
  `,
})
export class PatientListComponent implements OnInit {
  private readonly patientService = inject(PatientService);

  readonly patients = signal<Patient[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = ['uhid', 'name', 'ageGender', 'phone', 'actions'];

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.loading.set(true);
    const request$ = this.searchTerm()
      ? this.patientService.search(this.searchTerm(), this.currentPage(), this.pageSize())
      : this.patientService.getAll(this.currentPage(), this.pageSize());

    request$.subscribe({
      next: (response) => {
        this.patients.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadPatients();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadPatients();
  }

  formatAge(patient: Patient): string {
    if (patient.ageYears != null && patient.ageYears > 0) {
      return `${patient.ageYears}Y`;
    }
    if (patient.ageMonths != null && patient.ageMonths > 0) {
      return `${patient.ageMonths}M`;
    }
    if (patient.ageDays != null && patient.ageDays > 0) {
      return `${patient.ageDays}D`;
    }
    return '-';
  }

  formatGender(gender: string): string {
    return gender?.charAt(0) ?? '-';
  }
}
