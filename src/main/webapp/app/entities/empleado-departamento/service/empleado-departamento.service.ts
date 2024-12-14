import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpleadoDepartamento, NewEmpleadoDepartamento } from '../empleado-departamento.model';

export type PartialUpdateEmpleadoDepartamento = Partial<IEmpleadoDepartamento> & Pick<IEmpleadoDepartamento, 'id'>;

export type EntityResponseType = HttpResponse<IEmpleadoDepartamento>;
export type EntityArrayResponseType = HttpResponse<IEmpleadoDepartamento[]>;

@Injectable({ providedIn: 'root' })
export class EmpleadoDepartamentoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empleado-departamentos');

  create(empleadoDepartamento: NewEmpleadoDepartamento): Observable<EntityResponseType> {
    return this.http.post<IEmpleadoDepartamento>(this.resourceUrl, empleadoDepartamento, { observe: 'response' });
  }

  update(empleadoDepartamento: IEmpleadoDepartamento): Observable<EntityResponseType> {
    return this.http.put<IEmpleadoDepartamento>(
      `${this.resourceUrl}/${this.getEmpleadoDepartamentoIdentifier(empleadoDepartamento)}`,
      empleadoDepartamento,
      { observe: 'response' },
    );
  }

  partialUpdate(empleadoDepartamento: PartialUpdateEmpleadoDepartamento): Observable<EntityResponseType> {
    return this.http.patch<IEmpleadoDepartamento>(
      `${this.resourceUrl}/${this.getEmpleadoDepartamentoIdentifier(empleadoDepartamento)}`,
      empleadoDepartamento,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmpleadoDepartamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmpleadoDepartamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmpleadoDepartamentoIdentifier(empleadoDepartamento: Pick<IEmpleadoDepartamento, 'id'>): number {
    return empleadoDepartamento.id;
  }

  compareEmpleadoDepartamento(o1: Pick<IEmpleadoDepartamento, 'id'> | null, o2: Pick<IEmpleadoDepartamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpleadoDepartamentoIdentifier(o1) === this.getEmpleadoDepartamentoIdentifier(o2) : o1 === o2;
  }

  addEmpleadoDepartamentoToCollectionIfMissing<Type extends Pick<IEmpleadoDepartamento, 'id'>>(
    empleadoDepartamentoCollection: Type[],
    ...empleadoDepartamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empleadoDepartamentos: Type[] = empleadoDepartamentosToCheck.filter(isPresent);
    if (empleadoDepartamentos.length > 0) {
      const empleadoDepartamentoCollectionIdentifiers = empleadoDepartamentoCollection.map(empleadoDepartamentoItem =>
        this.getEmpleadoDepartamentoIdentifier(empleadoDepartamentoItem),
      );
      const empleadoDepartamentosToAdd = empleadoDepartamentos.filter(empleadoDepartamentoItem => {
        const empleadoDepartamentoIdentifier = this.getEmpleadoDepartamentoIdentifier(empleadoDepartamentoItem);
        if (empleadoDepartamentoCollectionIdentifiers.includes(empleadoDepartamentoIdentifier)) {
          return false;
        }
        empleadoDepartamentoCollectionIdentifiers.push(empleadoDepartamentoIdentifier);
        return true;
      });
      return [...empleadoDepartamentosToAdd, ...empleadoDepartamentoCollection];
    }
    return empleadoDepartamentoCollection;
  }
}
