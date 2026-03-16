import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { BranchService } from '../services/branch.service';

/**
 * Adds the X-Branch-Id header to all outgoing HTTP requests.
 * This is REQUIRED for multi-branch support.
 *
 * The backend uses this header to scope all data to the current branch.
 */
export const branchInterceptor: HttpInterceptorFn = (req, next) => {
  const branchService = inject(BranchService);
  const currentBranch = branchService.currentBranch();

  if (currentBranch) {
    const clonedReq = req.clone({
      headers: req.headers.set('X-Branch-Id', currentBranch.id),
    });
    return next(clonedReq);
  }

  return next(req);
};
