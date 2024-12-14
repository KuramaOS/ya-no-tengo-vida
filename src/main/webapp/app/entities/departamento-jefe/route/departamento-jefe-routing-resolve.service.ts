import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepartamentoJefe } from '../departamento-jefe.model';
import { DepartamentoJefeService } from '../service/departamento-jefe.service';

const departamentoJefeResolve = (route: ActivatedRouteSnapshot): Observable<null | IDepartamentoJefe> => {
  const id = route.params.id;
  if (id) {
    return inject(DepartamentoJefeService)
      .find(id)
      .pipe(
        mergeMap((departamentoJefe: HttpResponse<IDepartamentoJefe>) => {
          if (departamentoJefe.body) {
            return of(departamentoJefe.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default departamentoJefeResolve;
