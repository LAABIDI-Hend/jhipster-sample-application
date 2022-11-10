import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDate, NewDate } from '../date.model';

export type PartialUpdateDate = Partial<IDate> & Pick<IDate, 'id'>;

export type EntityResponseType = HttpResponse<IDate>;
export type EntityArrayResponseType = HttpResponse<IDate[]>;

@Injectable({ providedIn: 'root' })
export class DateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(date: NewDate): Observable<EntityResponseType> {
    return this.http.post<IDate>(this.resourceUrl, date, { observe: 'response' });
  }

  update(date: IDate): Observable<EntityResponseType> {
    return this.http.put<IDate>(`${this.resourceUrl}/${this.getDateIdentifier(date)}`, date, { observe: 'response' });
  }

  partialUpdate(date: PartialUpdateDate): Observable<EntityResponseType> {
    return this.http.patch<IDate>(`${this.resourceUrl}/${this.getDateIdentifier(date)}`, date, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDateIdentifier(date: Pick<IDate, 'id'>): number {
    return date.id;
  }

  compareDate(o1: Pick<IDate, 'id'> | null, o2: Pick<IDate, 'id'> | null): boolean {
    return o1 && o2 ? this.getDateIdentifier(o1) === this.getDateIdentifier(o2) : o1 === o2;
  }

  addDateToCollectionIfMissing<Type extends Pick<IDate, 'id'>>(
    dateCollection: Type[],
    ...datesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dates: Type[] = datesToCheck.filter(isPresent);
    if (dates.length > 0) {
      const dateCollectionIdentifiers = dateCollection.map(dateItem => this.getDateIdentifier(dateItem)!);
      const datesToAdd = dates.filter(dateItem => {
        const dateIdentifier = this.getDateIdentifier(dateItem);
        if (dateCollectionIdentifiers.includes(dateIdentifier)) {
          return false;
        }
        dateCollectionIdentifiers.push(dateIdentifier);
        return true;
      });
      return [...datesToAdd, ...dateCollection];
    }
    return dateCollection;
  }
}
