import { Pipe, PipeTransform } from '@angular/core';

/**
 * Formats currency values for LIS billing display.
 *
 * Usage:
 *   {{ invoice.amount | currencyFormat }}         → '₹1,250.00'
 *   {{ invoice.amount | currencyFormat:'USD' }}   → '$1,250.00'
 */
@Pipe({
  name: 'currencyFormat',
  standalone: true,
})
export class CurrencyFormatPipe implements PipeTransform {
  transform(value: number | string | null | undefined, currency = 'INR', locale = 'en-IN'): string {
    if (value === null || value === undefined) return '—';

    const numValue = typeof value === 'string' ? parseFloat(value) : value;
    if (isNaN(numValue)) return '—';

    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(numValue);
  }
}
