import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDate } from '../date.model';

@Component({
  selector: 'jhi-date-detail',
  templateUrl: './date-detail.component.html',
})
export class DateDetailComponent implements OnInit {
  date: IDate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ date }) => {
      this.date = date;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
