import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { EmpleadoDepartamentoService } from '../service/empleado-departamento.service';
import { IEmpleadoDepartamento } from '../empleado-departamento.model';
import { EmpleadoDepartamentoFormGroup, EmpleadoDepartamentoFormService } from './empleado-departamento-form.service';

@Component({
  standalone: true,
  selector: 'jhi-empleado-departamento-update',
  templateUrl: './empleado-departamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadoDepartamentoUpdateComponent implements OnInit {
  isSaving = false;
  empleadoDepartamento: IEmpleadoDepartamento | null = null;

  empleadosSharedCollection: IEmpleado[] = [];
  departamentosSharedCollection: IDepartamento[] = [];

  protected empleadoDepartamentoService = inject(EmpleadoDepartamentoService);
  protected empleadoDepartamentoFormService = inject(EmpleadoDepartamentoFormService);
  protected empleadoService = inject(EmpleadoService);
  protected departamentoService = inject(DepartamentoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadoDepartamentoFormGroup = this.empleadoDepartamentoFormService.createEmpleadoDepartamentoFormGroup();

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleadoDepartamento }) => {
      this.empleadoDepartamento = empleadoDepartamento;
      if (empleadoDepartamento) {
        this.updateForm(empleadoDepartamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleadoDepartamento = this.empleadoDepartamentoFormService.getEmpleadoDepartamento(this.editForm);
    if (empleadoDepartamento.id !== null) {
      this.subscribeToSaveResponse(this.empleadoDepartamentoService.update(empleadoDepartamento));
    } else {
      this.subscribeToSaveResponse(this.empleadoDepartamentoService.create(empleadoDepartamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleadoDepartamento>>): void {
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

  protected updateForm(empleadoDepartamento: IEmpleadoDepartamento): void {
    this.empleadoDepartamento = empleadoDepartamento;
    this.empleadoDepartamentoFormService.resetForm(this.editForm, empleadoDepartamento);

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      empleadoDepartamento.empleado,
    );
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      empleadoDepartamento.departamento,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.empleadoDepartamento?.empleado),
        ),
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));

    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
            departamentos,
            this.empleadoDepartamento?.departamento,
          ),
        ),
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));
  }
}
