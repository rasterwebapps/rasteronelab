# Generate Angular Feature Module — /generate-angular-module

Generate a complete Angular 19 feature module using standalone components, Signals, and Angular Material.

## Usage
```
/generate-angular-module {featureName} {entity} {fields...}
```

## Example
```
/generate-angular-module patient Patient "id, fullName, uhid, gender, dateOfBirth, phoneNumber, email, branchId"
```

## What Gets Generated

### 1. Routes (`features/{feature}/routes.ts`)
```typescript
export const PATIENT_ROUTES: Routes = [
  {
    path: '',
    children: [
      { path: '', component: PatientListPageComponent },
      { path: 'new', component: PatientFormPageComponent },
      { path: ':id', component: PatientDetailPageComponent },
      { path: ':id/edit', component: PatientFormPageComponent },
    ]
  }
];
```

### 2. Model Interface (`features/{feature}/models/{entity}.model.ts`)
```typescript
export interface Patient {
  id: string;
  fullName: string;
  uhid: string;
  gender: Gender;
  dateOfBirth: string; // ISO date string
  phoneNumber: string;
  email?: string;
  branchId: string;
  createdAt: string;
  updatedAt: string;
}

export interface PatientRequest {
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  phoneNumber: string;
  email?: string;
}

export enum Gender { MALE = 'MALE', FEMALE = 'FEMALE', OTHER = 'OTHER' }
```

### 3. Service (`features/{feature}/services/{entity}.service.ts`)
```typescript
@Injectable({ providedIn: 'root' })
export class PatientService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/patients`;

  getAll(params?: PatientSearchParams): Observable<PagedResponse<Patient>> {
    return this.http.get<ApiResponse<PagedResponse<Patient>>>(this.baseUrl, { params }).pipe(
      map(r => r.data)
    );
  }
  
  getById(id: string): Observable<Patient> { ... }
  create(request: PatientRequest): Observable<Patient> { ... }
  update(id: string, request: PatientRequest): Observable<Patient> { ... }
  delete(id: string): Observable<void> { ... }
}
```

### 4. SignalStore (`features/{feature}/store/{entity}.store.ts`)
```typescript
export const PatientStore = signalStore(
  { providedIn: 'root' },
  withState<PatientState>(initialState),
  withComputed(({ patients, loading, selectedPatient }) => ({
    hasPatients: computed(() => patients().length > 0),
    isLoading: computed(() => loading()),
  })),
  withMethods((store, patientService = inject(PatientService)) => ({
    loadPatients: rxMethod<PatientSearchParams>(
      pipe(
        debounceTime(300),
        switchMap((params) => patientService.getAll(params).pipe(
          tapResponse({
            next: (response) => patchState(store, { patients: response.content, total: response.totalElements }),
            error: (error) => patchState(store, { error: error.message }),
          })
        ))
      )
    ),
    // ... other methods
  }))
);
```

### 5. List Page (Smart Component)
```typescript
@Component({
  selector: 'app-patient-list-page',
  standalone: true,
  imports: [PatientListComponent, MatButtonModule, RouterLink, ...],
  template: `
    <app-page-header title="Patients" [breadcrumbs]="breadcrumbs">
      <button mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> New Patient
      </button>
    </app-page-header>
    
    <app-patient-list 
      [patients]="store.patients()"
      [loading]="store.isLoading()"
      (search)="onSearch($event)"
      (edit)="onEdit($event)"
      (delete)="onDelete($event)" />
  `
})
```

### 6. Form Page (Smart Component)
- Reactive form with `FormBuilder`
- Create and edit modes
- Form validation with `@if` for error messages
- Submit using store method

### 7. Detail Page (Smart Component)
- Display all entity fields
- Action buttons (edit, delete)
- Related data sections

### 8. List Component (Dumb/Presentational)
```typescript
@Component({
  selector: 'app-patient-list',
  standalone: true,
  inputs: ['patients', 'loading'],
  outputs: ['search', 'edit', 'delete'],
  // Uses @for, @if new control flow
})
```

### 9. Tests (`__tests__/`)
- Service test with HttpClientTestingModule
- Store test with mock service
- Component tests with Angular Testing utilities
