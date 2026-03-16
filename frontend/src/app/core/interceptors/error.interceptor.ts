import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

/**
 * Global HTTP error handler.
 * Shows user-friendly error messages for common HTTP errors.
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unexpected error occurred';

      if (error.error?.message) {
        errorMessage = error.error.message;
      } else {
        switch (error.status) {
          case 400:
            errorMessage = 'Invalid request. Please check your input.';
            break;
          case 403:
            errorMessage = 'You do not have permission to perform this action.';
            break;
          case 404:
            errorMessage = 'The requested resource was not found.';
            break;
          case 422:
            errorMessage = error.error?.message || 'Validation failed.';
            break;
          case 500:
            errorMessage = 'Server error. Please try again later.';
            break;
          case 503:
            errorMessage = 'Service unavailable. Please try again later.';
            break;
        }
      }

      if (error.status !== 401) {
        notificationService.showError(errorMessage);
      }

      return throwError(() => error);
    })
  );
};
