import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { environment } from '@environments/environment';

export interface Branch {
  id: string;
  code: string;
  name: string;
  city: string;
  state: string;
  phone: string;
  organizationId: string;
}

/**
 * Manages the current branch context for multi-branch support.
 * The selected branch ID is sent with every API request via BranchInterceptor.
 */
@Injectable({ providedIn: 'root' })
export class BranchService {
  private readonly http = inject(HttpClient);
  private readonly _currentBranch = signal<Branch | null>(null);
  private readonly _availableBranches = signal<Branch[]>([]);

  readonly currentBranch = this._currentBranch.asReadonly();
  readonly availableBranches = this._availableBranches.asReadonly();
  readonly hasBranchSelected = computed(() => this._currentBranch() !== null);

  constructor() {
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const branchJson = sessionStorage.getItem('current_branch');
    if (branchJson) {
      this._currentBranch.set(JSON.parse(branchJson));
    }
  }

  loadBranches(): void {
    this.http.get<{ data: Branch[] }>(`${environment.apiUrl}/admin/branches/my-branches`)
      .pipe(
        tap(response => this._availableBranches.set(response.data))
      )
      .subscribe();
  }

  selectBranch(branch: Branch): void {
    this._currentBranch.set(branch);
    sessionStorage.setItem('current_branch', JSON.stringify(branch));
  }

  clearBranch(): void {
    this._currentBranch.set(null);
    sessionStorage.removeItem('current_branch');
  }
}
