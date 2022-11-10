import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DateComponent } from './list/date.component';
import { DateDetailComponent } from './detail/date-detail.component';
import { DateUpdateComponent } from './update/date-update.component';
import { DateDeleteDialogComponent } from './delete/date-delete-dialog.component';
import { DateRoutingModule } from './route/date-routing.module';

@NgModule({
  imports: [SharedModule, DateRoutingModule],
  declarations: [DateComponent, DateDetailComponent, DateUpdateComponent, DateDeleteDialogComponent],
})
export class DateModule {}
