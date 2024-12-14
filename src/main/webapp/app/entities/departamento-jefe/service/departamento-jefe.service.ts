import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartamentoJefe, NewDepartamentoJefe } from '../departamento-jefe.model';

export type PartialUpdateDepartamentoJefe = Partial<IDepartamentoJefe> & Pick<IDepartamentoJefe, 'id'>;

export type EntityResponseType = HttpResponse<IDepartamentoJefe>;
export type EntityArrayResponseType = HttpResponse<IDepartamentoJefe[]>;

@Injectable({ providedIn: 'root' })
export class DepartamentoJefeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/departamento-jefes');

  create(departamentoJefe: NewDepartamentoJefe): Observable<EntityResponseType> {
    return this.http.post<IDepartamentoJefe>(this.resourceUrl, departamentoJefe, { observe: 'response' });
  }

  update(departamentoJefe: IDepartamentoJefe): Observable<EntityResponseType> {
    return this.http.put<IDepartamentoJefe>(
      `${this.resourceUrl}/${this.getDepartamentoJefeIdentifier(departamentoJefe)}`,
      departamentoJefe,
      { observe: 'response' },
    );
  }

  partialUpdate(departamentoJefe: PartialUpdateDepartamentoJefe): Observable<EntityResponseType> {
    return this.http.patch<IDepartamentoJefe>(
      `${this.resourceUrl}/${this.getDepartamentoJefeIdentifier(departamentoJefe)}`,
      departamentoJefe,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartamentoJefe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartamentoJefe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDepartamentoJefeIdentifier(departamentoJefe: Pick<IDepartamentoJefe, 'id'>): number {
    return departamentoJefe.id;
  }

  compareDepartamentoJefe(o1: Pick<IDepartamentoJefe, 'id'> | null, o2: Pick<IDepartamentoJefe, 'id'> | null): boolean {
    return o1 && o2 ? this.getDepartamentoJefeIdentifier(o1) === this.getDepartamentoJefeIdentifier(o2) : o1 === o2;
  }

  addDepartamentoJefeToCollectionIfMissing<Type extends Pick<IDepartamentoJefe, 'id'>>(
    departamentoJefeCollection: Type[],
    ...departamentoJefesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const departamentoJefes: Type[] = departamentoJefesToCheck.filter(isPresent);
    if (departamentoJefes.length > 0) {
      const departamentoJefeCollectionIdentifiers = departamentoJefeCollection.map(departamentoJefeItem =>
        this.getDepartamentoJefeIdentifier(departamentoJefeItem),
      );
      const departamentoJefesToAdd = departamentoJefes.filter(departamentoJefeItem => {
        const departamentoJefeIdentifier = this.getDepartamentoJefeIdentifier(departamentoJefeItem);
        if (departamentoJefeCollectionIdentifiers.includes(departamentoJefeIdentifier)) {
          return false;
        }
        departamentoJefeCollectionIdentifiers.push(departamentoJefeIdentifier);
        return true;
      });
      return [...departamentoJefesToAdd, ...departamentoJefeCollection];
    }
    return departamentoJefeCollection;
  }
}
