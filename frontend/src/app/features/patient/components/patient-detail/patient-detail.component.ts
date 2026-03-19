import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { PatientService } from '../../services/patient.service';
import { Patient, PatientVisit } from '../../models/patient.model';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [
    RouterLink, MatCardModule, MatTabsModule, MatButtonModule,
    MatIconModule, MatChipsModule, MatDividerModule,
    MatProgressSpinnerModule, MatTableModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else if (patient()) {
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-xl font-bold text-gray-800">{{ patient()!.firstName }} {{ patient()!.lastName }}</h2>
          <p class="text-gray-500 text-sm">UHID: {{ patient()!.uhid }}</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/patient">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>
          <a mat-raised-button color="primary" [routerLink]="['/patient', patient()!.id, 'edit']">
            <mat-icon>edit</mat-icon> Edit
          </a>
        </div>
      </div>

      <mat-tab-group animationDuration="200ms">
        <mat-tab label="Demographics">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
            <mat-card>
              <mat-card-header>
                <mat-card-title>Personal Information</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Name</div>
                <div>{{ patient()!.firstName }} {{ patient()!.lastName }}</div>
                <div class="text-gray-500 text-sm">Gender</div>
                <div>{{ patient()!.gender }}</div>
                <div class="text-gray-500 text-sm">Date of Birth</div>
                <div>{{ patient()!.dateOfBirth ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Age</div>
                <div>{{ formatAge(patient()!) }}</div>
                <div class="text-gray-500 text-sm">Blood Group</div>
                <div>{{ patient()!.bloodGroup ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Nationality</div>
                <div>{{ patient()!.nationality ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Contact Information</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Phone</div>
                <div>{{ patient()!.phone }}</div>
                <div class="text-gray-500 text-sm">Email</div>
                <div>{{ patient()!.email ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Address</div>
                <div>
                  {{ patient()!.addressLine1 ?? '' }}
                  @if (patient()!.addressLine2) { , {{ patient()!.addressLine2 }} }
                </div>
                <div class="text-gray-500 text-sm">City / State</div>
                <div>{{ patient()!.city ?? '' }} {{ patient()!.state ? ', ' + patient()!.state : '' }}</div>
                <div class="text-gray-500 text-sm">Postal Code</div>
                <div>{{ patient()!.postalCode ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Emergency Contact</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Name</div>
                <div>{{ patient()!.emergencyContactName ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Phone</div>
                <div>{{ patient()!.emergencyContactPhone ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Identification</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">ID Type</div>
                <div>{{ patient()!.idType ?? '-' }}</div>
                <div class="text-gray-500 text-sm">ID Number</div>
                <div>{{ patient()!.idNumber ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Notes</div>
                <div>{{ patient()!.notes ?? '-' }}</div>
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Visit History">
          <div class="mt-4">
            <mat-card>
              <mat-card-content>
                @if (visits().length > 0) {
                  <table mat-table [dataSource]="visits()" class="w-full">
                    <ng-container matColumnDef="visitNumber">
                      <th mat-header-cell *matHeaderCellDef>Visit #</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitNumber }}</td>
                    </ng-container>
                    <ng-container matColumnDef="visitDate">
                      <th mat-header-cell *matHeaderCellDef>Date</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitDate }}</td>
                    </ng-container>
                    <ng-container matColumnDef="visitType">
                      <th mat-header-cell *matHeaderCellDef>Type</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitType }}</td>
                    </ng-container>
                    <ng-container matColumnDef="clinicalNotes">
                      <th mat-header-cell *matHeaderCellDef>Notes</th>
                      <td mat-cell *matCellDef="let row">{{ row.clinicalNotes ?? '-' }}</td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="visitColumns"></tr>
                    <tr mat-row *matRowDef="let row; columns: visitColumns"></tr>
                  </table>
                } @else {
                  <div class="text-center py-8 text-gray-400">
                    <mat-icon class="!text-5xl mb-2">event_note</mat-icon>
                    <p>No visits recorded</p>
                  </div>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Orders">
          <div class="text-center py-8 text-gray-400 mt-4">
            <mat-icon class="!text-5xl mb-2">science</mat-icon>
            <p>Order history coming soon</p>
          </div>
        </mat-tab>

        <mat-tab label="Reports">
          <div class="text-center py-8 text-gray-400 mt-4">
            <mat-icon class="!text-5xl mb-2">description</mat-icon>
            <p>Reports coming soon</p>
          </div>
        </mat-tab>

        <mat-tab label="Billing">
          <div class="text-center py-8 text-gray-400 mt-4">
            <mat-icon class="!text-5xl mb-2">receipt_long</mat-icon>
            <p>Billing history coming soon</p>
          </div>
        </mat-tab>
      </mat-tab-group>
    }
  `,
})
export class PatientDetailComponent implements OnInit {
  private readonly patientService = inject(PatientService);
  private readonly route = inject(ActivatedRoute);

  readonly patient = signal<Patient | null>(null);
  readonly visits = signal<PatientVisit[]>([]);
  readonly loading = signal(false);

  readonly visitColumns = ['visitNumber', 'visitDate', 'visitType', 'clinicalNotes'];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadPatient(id);
      this.loadVisits(id);
    }
  }

  private loadPatient(id: string): void {
    this.loading.set(true);
    this.patientService.getById(id).subscribe({
      next: (response) => {
        this.patient.set(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  private loadVisits(patientId: string): void {
    this.patientService.getVisits(patientId).subscribe({
      next: (response) => this.visits.set(response.data ?? []),
    });
  }

  formatAge(patient: Patient): string {
    const parts: string[] = [];
    if (patient.ageYears != null && patient.ageYears > 0) parts.push(`${patient.ageYears}Y`);
    if (patient.ageMonths != null && patient.ageMonths > 0) parts.push(`${patient.ageMonths}M`);
    if (patient.ageDays != null && patient.ageDays > 0) parts.push(`${patient.ageDays}D`);
    return parts.length > 0 ? parts.join(' ') : '-';
  }
}
