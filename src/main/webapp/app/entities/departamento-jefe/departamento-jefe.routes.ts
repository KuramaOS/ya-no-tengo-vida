import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DepartamentoJefeResolve from './route/departamento-jefe-routing-resolve.service';

const departamentoJefeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/departamento-jefe.component').then(m => m.DepartamentoJefeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/departamento-jefe-detail.component').then(m => m.DepartamentoJefeDetailComponent),
    resolve: {
      departamentoJefe: DepartamentoJefeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/departamento-jefe-update.component').then(m => m.DepartamentoJefeUpdateComponent),
    resolve: {
      departamentoJefe: DepartamentoJefeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/departamento-jefe-update.component').then(m => m.DepartamentoJefeUpdateComponent),
    resolve: {
      departamentoJefe: DepartamentoJefeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default departamentoJefeRoute;
