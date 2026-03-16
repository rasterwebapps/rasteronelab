import { Pipe, PipeTransform } from '@angular/core';
import { format, parseISO, isValid } from 'date-fns';

/**
 * Formats date strings using date-fns.
 * Uses date-fns (NOT Moment.js) as per project standards.
 *
 * Usage:
 *   {{ patient.dateOfBirth | dateFormat }}          → '15 Jan 1990'
 *   {{ order.createdAt | dateFormat:'dd/MM/yyyy' }} → '15/01/2024'
 *   {{ result.analyzedAt | dateFormat:'datetime' }} → '15 Jan 2024, 14:30'
 */
@Pipe({
  name: 'dateFormat',
  standalone: true,
})
export class DateFormatPipe implements PipeTransform {
  transform(value: string | Date | null | undefined, formatStr = 'dd MMM yyyy'): string {
    if (!value) return '—';

    const date = typeof value === 'string' ? parseISO(value) : value;
    if (!isValid(date)) return '—';

    if (formatStr === 'datetime') {
      return format(date, 'dd MMM yyyy, HH:mm');
    }
    if (formatStr === 'time') {
      return format(date, 'HH:mm');
    }
    if (formatStr === 'relative') {
      return format(date, 'dd MMM yyyy');
    }

    return format(date, formatStr);
  }
}
