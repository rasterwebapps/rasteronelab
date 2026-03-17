import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [MatIconModule, RouterLink],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nav class="flex items-center gap-1 text-sm text-gray-500 py-2 px-4">
      <a routerLink="/dashboard" class="flex items-center hover:text-indigo-600">
        <mat-icon class="!text-base !h-4 !w-4">home</mat-icon>
      </a>
      @for (crumb of breadcrumbs(); track crumb.url) {
        <mat-icon class="!text-base !h-4 !w-4">chevron_right</mat-icon>
        <a [routerLink]="crumb.url" class="hover:text-indigo-600 capitalize">
          {{ crumb.label }}
        </a>
      }
    </nav>
  `,
})
export class BreadcrumbComponent {
  private readonly router = inject(Router);
  private readonly activatedRoute = inject(ActivatedRoute);

  readonly breadcrumbs = toSignal(
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map(() => this.buildBreadcrumbs(this.activatedRoute.root)),
    ),
    { initialValue: [] as Breadcrumb[] },
  );

  private buildBreadcrumbs(route: ActivatedRoute, url = '', crumbs: Breadcrumb[] = []): Breadcrumb[] {
    const children = route.children;
    if (children.length === 0) return crumbs;

    for (const child of children) {
      const segments = child.snapshot.url.map(s => s.path);
      if (segments.length === 0) {
        return this.buildBreadcrumbs(child, url, crumbs);
      }

      const childUrl = `${url}/${segments.join('/')}`;
      const label = child.snapshot.data['breadcrumb'] ?? segments[segments.length - 1];
      crumbs.push({ label, url: childUrl });
      return this.buildBreadcrumbs(child, childUrl, crumbs);
    }

    return crumbs;
  }
}
