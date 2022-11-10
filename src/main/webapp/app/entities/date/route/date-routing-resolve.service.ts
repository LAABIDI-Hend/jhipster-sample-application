import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDate } from '../date.model';
import { DateService } from '../service/date.service';

@Injectable({ providedIn: 'root' })
export class DateRoutingResolveService implements Resolve<IDate | null> {
  constructor(protected service: DateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDate | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((date: HttpResponse<IDate>) => {
          if (date.body) {
            return of(date.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
