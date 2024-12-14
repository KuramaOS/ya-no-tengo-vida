import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmpleadoDepartamento } from '../empleado-departamento.model';
import { EmpleadoDepartamentoService } from '../service/empleado-departamento.service';

const empleadoDepartamentoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmpleadoDepartamento> => {
  const id = route.params.id;
  if (id) {
    return inject(EmpleadoDepartamentoService)
      .find(id)
      .pipe(
        mergeMap((empleadoDepartamento: HttpResponse<IEmpleadoDepartamento>) => {
          if (empleadoDepartamento.body) {
            return of(empleadoDepartamento.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default empleadoDepartamentoResolve;
