# Frontend — Angular 19 LIS Application

## Build Commands
```bash
ng serve                                 # Dev server http://localhost:4200
ng build --configuration=production      # Production build
ng test                                  # Unit tests (Karma + Jasmine)
ng test --code-coverage                  # With coverage report (target: 75%)
ng e2e                                   # Cypress E2E tests
ng lint                                  # ESLint
ng generate component features/patient/components/patient-form --standalone
```

## Key Dependencies
- Angular 19 (standalone components, Signals, new control flow)
- Angular Material 19 (UI components)
- Tailwind CSS 4 (utility classes)
- @ngrx/signals (SignalStore for complex state)
- date-fns 4 (date formatting — NOT Moment.js)
- chart.js 4 (QC Levey-Jennings charts, CBC histograms)
- ngx-extended-pdf-viewer (report viewer)
- socket.io-client (real-time instrument results)
- rxjs 7 (HTTP, not for state)

## Component Generation Pattern

### Full Feature Module (use /generate-angular-module)
1. `features/{feature}/routes.ts` — Lazy-loaded routes
2. `features/{feature}/models/{entity}.model.ts` — TypeScript interfaces
3. `features/{feature}/services/{entity}.service.ts` — HTTP service
4. `features/{feature}/store/{entity}.store.ts` — SignalStore (if complex state)
5. `features/{feature}/pages/{entity}-list.page.ts` — Smart container
6. `features/{feature}/pages/{entity}-form.page.ts` — Smart form container
7. `features/{feature}/components/{entity}-list.component.ts` — Dumb list
8. `features/{feature}/components/{entity}-form.component.ts` — Dumb form

### Angular 19 Patterns
```typescript
// ✅ CORRECT: Standalone component with inject()
@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, RouterLink, DateFormatPipe, AsyncPipe],
  templateUrl: './patient-list.component.html',
})
export class PatientListComponent {
  private readonly patientService = inject(PatientService);
  private readonly router = inject(Router);
  
  // Signals for state
  readonly patients = signal<Patient[]>([]);
  readonly loading = signal(false);
  readonly searchQuery = signal('');
  
  // Computed values
  readonly filteredPatients = computed(() =>
    this.patients().filter(p => 
      p.fullName.toLowerCase().includes(this.searchQuery().toLowerCase())
    )
  );
}

// ✅ CORRECT: New control flow in templates
// @if instead of *ngIf
// @for instead of *ngFor
// @switch instead of *ngSwitch
```

### Template Patterns
```html
<!-- ✅ Use @if (not *ngIf) -->
@if (loading()) {
  <app-loading-spinner />
} @else if (patients().length === 0) {
  <p class="text-gray-500">No patients found</p>
} @else {
  <app-patient-list [patients]="patients()" />
}

<!-- ✅ Use @for (not *ngFor) -->
@for (patient of patients(); track patient.id) {
  <app-patient-card [patient]="patient" />
} @empty {
  <p>No patients</p>
}
```

## CRITICAL Rules
1. **NO NgModules** — everything must be `standalone: true`
2. **NO `*ngIf`/`*ngFor`** — use `@if`/`@for` new control flow
3. **NO Moment.js** — use `date-fns` only
4. **NO RxJS Subjects for state** — use Angular Signals
5. **NO `any` type** — always use proper TypeScript interfaces
6. **NO `*` for HttpClient standalone** — use `withInterceptors` in app.config.ts
7. Always add `X-Branch-Id` header via BranchInterceptor (automatic)
8. Use `inject()` function, not constructor injection in components

## Department-Specific Result Entry Components

Each laboratory department has a specialized result entry UI:

| Department | Component | Special Features |
|-----------|-----------|-----------------|
| Biochemistry | `BiochemResultEntryComponent` | Tab-through numeric grid, auto-flag H/L, auto-calculate derived |
| Hematology | `HematoResultEntryComponent` | CBC table + differential % (sum=100 validation) + scatter plot upload |
| Microbiology | `CultureResultEntryComponent` | Multi-stage: Day1/Day2/Day3 tabs, antibiogram matrix grid |
| Histopathology | `HistoResultEntryComponent` | Rich text editor (TipTap/Quill) + image upload sections |
| Serology | `SerologyResultEntryComponent` | Reactive/Non-Reactive dropdown + titer input |
| Molecular | `MolecularResultEntryComponent` | CT value inputs + amplification curve image |

### Result Entry Factory Pattern
```typescript
// result-entry-factory.service.ts
@Injectable({ providedIn: 'root' })
export class ResultEntryFactory {
  getComponent(department: string): Type<unknown> {
    const componentMap: Record<string, Type<unknown>> = {
      'BIOCHEMISTRY': BiochemResultEntryComponent,
      'HEMATOLOGY': HematoResultEntryComponent,
      'MICROBIOLOGY': CultureResultEntryComponent,
      'HISTOPATHOLOGY': HistoResultEntryComponent,
      'SEROLOGY': SerologyResultEntryComponent,
      'MOLECULAR': MolecularResultEntryComponent,
    };
    return componentMap[department] ?? DefaultResultEntryComponent;
  }
}
```
