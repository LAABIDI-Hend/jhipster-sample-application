import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DateComponent } from '../list/date.component';
import { DateDetailComponent } from '../detail/date-detail.component';
import { DateUpdateComponent } from '../update/date-update.component';
import { DateRoutingResolveService } from './date-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dateRoute: Routes = [
  {
    path: '',
    component: DateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DateDetailComponent,
    resolve: {
      date: DateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DateUpdateComponent,
    resolve: {
      date: DateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DateUpdateComponent,
    resolve: {
      date: DateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dateRoute)],
  exports: [RouterModule],
})
export class DateRoutingModule {}
