import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DateFormService, DateFormGroup } from './date-form.service';
import { IDate } from '../date.model';
import { DateService } from '../service/date.service';

@Component({
  selector: 'jhi-date-update',
  templateUrl: './date-update.component.html',
})
export class DateUpdateComponent implements OnInit {
  isSaving = false;
  date: IDate | null = null;

  editForm: DateFormGroup = this.dateFormService.createDateFormGroup();

  constructor(protected dateService: DateService, protected dateFormService: DateFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ date }) => {
      this.date = date;
      if (date) {
        this.updateForm(date);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const date = this.dateFormService.getDate(this.editForm);
    if (date.id !== null) {
      this.subscribeToSaveResponse(this.dateService.update(date));
    } else {
      this.subscribeToSaveResponse(this.dateService.create(date));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(date: IDate): void {
    this.date = date;
    this.dateFormService.resetForm(this.editForm, date);
  }
}
