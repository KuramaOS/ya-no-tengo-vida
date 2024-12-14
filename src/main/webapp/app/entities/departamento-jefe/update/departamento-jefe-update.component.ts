import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IJefe } from 'app/entities/jefe/jefe.model';
import { JefeService } from 'app/entities/jefe/service/jefe.service';
import { DepartamentoJefeService } from '../service/departamento-jefe.service';
import { IDepartamentoJefe } from '../departamento-jefe.model';
import { DepartamentoJefeFormGroup, DepartamentoJefeFormService } from './departamento-jefe-form.service';

@Component({
  standalone: true,
  selector: 'jhi-departamento-jefe-update',
  templateUrl: './departamento-jefe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartamentoJefeUpdateComponent implements OnInit {
  isSaving = false;
  departamentoJefe: IDepartamentoJefe | null = null;

  departamentosSharedCollection: IDepartamento[] = [];
  jefesSharedCollection: IJefe[] = [];

  protected departamentoJefeService = inject(DepartamentoJefeService);
  protected departamentoJefeFormService = inject(DepartamentoJefeFormService);
  protected departamentoService = inject(DepartamentoService);
  protected jefeService = inject(JefeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartamentoJefeFormGroup = this.departamentoJefeFormService.createDepartamentoJefeFormGroup();

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  compareJefe = (o1: IJefe | null, o2: IJefe | null): boolean => this.jefeService.compareJefe(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamentoJefe }) => {
      this.departamentoJefe = departamentoJefe;
      if (departamentoJefe) {
        this.updateForm(departamentoJefe);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departamentoJefe = this.departamentoJefeFormService.getDepartamentoJefe(this.editForm);
    if (departamentoJefe.id !== null) {
      this.subscribeToSaveResponse(this.departamentoJefeService.update(departamentoJefe));
    } else {
      this.subscribeToSaveResponse(this.departamentoJefeService.create(departamentoJefe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartamentoJefe>>): void {
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

  protected updateForm(departamentoJefe: IDepartamentoJefe): void {
    this.departamentoJefe = departamentoJefe;
    this.departamentoJefeFormService.resetForm(this.editForm, departamentoJefe);

    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      departamentoJefe.departamento,
    );
    this.jefesSharedCollection = this.jefeService.addJefeToCollectionIfMissing<IJefe>(this.jefesSharedCollection, departamentoJefe.jefe);
  }

  protected loadRelationshipsOptions(): void {
    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.departamentoJefe?.departamento),
        ),
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));

    this.jefeService
      .query()
      .pipe(map((res: HttpResponse<IJefe[]>) => res.body ?? []))
      .pipe(map((jefes: IJefe[]) => this.jefeService.addJefeToCollectionIfMissing<IJefe>(jefes, this.departamentoJefe?.jefe)))
      .subscribe((jefes: IJefe[]) => (this.jefesSharedCollection = jefes));
  }
}
