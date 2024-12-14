import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDepartamentoJefe, NewDepartamentoJefe } from '../departamento-jefe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartamentoJefe for edit and NewDepartamentoJefeFormGroupInput for create.
 */
type DepartamentoJefeFormGroupInput = IDepartamentoJefe | PartialWithRequiredKeyOf<NewDepartamentoJefe>;

type DepartamentoJefeFormDefaults = Pick<NewDepartamentoJefe, 'id'>;

type DepartamentoJefeFormGroupContent = {
  id: FormControl<IDepartamentoJefe['id'] | NewDepartamentoJefe['id']>;
  idDepartamento: FormControl<IDepartamentoJefe['idDepartamento']>;
  idJefe: FormControl<IDepartamentoJefe['idJefe']>;
  departamento: FormControl<IDepartamentoJefe['departamento']>;
  jefe: FormControl<IDepartamentoJefe['jefe']>;
};

export type DepartamentoJefeFormGroup = FormGroup<DepartamentoJefeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepartamentoJefeFormService {
  createDepartamentoJefeFormGroup(departamentoJefe: DepartamentoJefeFormGroupInput = { id: null }): DepartamentoJefeFormGroup {
    const departamentoJefeRawValue = {
      ...this.getFormDefaults(),
      ...departamentoJefe,
    };
    return new FormGroup<DepartamentoJefeFormGroupContent>({
      id: new FormControl(
        { value: departamentoJefeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idDepartamento: new FormControl(departamentoJefeRawValue.idDepartamento, {
        validators: [Validators.required],
      }),
      idJefe: new FormControl(departamentoJefeRawValue.idJefe, {
        validators: [Validators.required],
      }),
      departamento: new FormControl(departamentoJefeRawValue.departamento),
      jefe: new FormControl(departamentoJefeRawValue.jefe),
    });
  }

  getDepartamentoJefe(form: DepartamentoJefeFormGroup): IDepartamentoJefe | NewDepartamentoJefe {
    return form.getRawValue() as IDepartamentoJefe | NewDepartamentoJefe;
  }

  resetForm(form: DepartamentoJefeFormGroup, departamentoJefe: DepartamentoJefeFormGroupInput): void {
    const departamentoJefeRawValue = { ...this.getFormDefaults(), ...departamentoJefe };
    form.reset(
      {
        ...departamentoJefeRawValue,
        id: { value: departamentoJefeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepartamentoJefeFormDefaults {
    return {
      id: null,
    };
  }
}
