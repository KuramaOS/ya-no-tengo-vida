import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmpleadoDepartamento, NewEmpleadoDepartamento } from '../empleado-departamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleadoDepartamento for edit and NewEmpleadoDepartamentoFormGroupInput for create.
 */
type EmpleadoDepartamentoFormGroupInput = IEmpleadoDepartamento | PartialWithRequiredKeyOf<NewEmpleadoDepartamento>;

type EmpleadoDepartamentoFormDefaults = Pick<NewEmpleadoDepartamento, 'id'>;

type EmpleadoDepartamentoFormGroupContent = {
  id: FormControl<IEmpleadoDepartamento['id'] | NewEmpleadoDepartamento['id']>;
  idEmpleado: FormControl<IEmpleadoDepartamento['idEmpleado']>;
  idDepartamento: FormControl<IEmpleadoDepartamento['idDepartamento']>;
  empleado: FormControl<IEmpleadoDepartamento['empleado']>;
  departamento: FormControl<IEmpleadoDepartamento['departamento']>;
};

export type EmpleadoDepartamentoFormGroup = FormGroup<EmpleadoDepartamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadoDepartamentoFormService {
  createEmpleadoDepartamentoFormGroup(
    empleadoDepartamento: EmpleadoDepartamentoFormGroupInput = { id: null },
  ): EmpleadoDepartamentoFormGroup {
    const empleadoDepartamentoRawValue = {
      ...this.getFormDefaults(),
      ...empleadoDepartamento,
    };
    return new FormGroup<EmpleadoDepartamentoFormGroupContent>({
      id: new FormControl(
        { value: empleadoDepartamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idEmpleado: new FormControl(empleadoDepartamentoRawValue.idEmpleado, {
        validators: [Validators.required],
      }),
      idDepartamento: new FormControl(empleadoDepartamentoRawValue.idDepartamento, {
        validators: [Validators.required],
      }),
      empleado: new FormControl(empleadoDepartamentoRawValue.empleado),
      departamento: new FormControl(empleadoDepartamentoRawValue.departamento),
    });
  }

  getEmpleadoDepartamento(form: EmpleadoDepartamentoFormGroup): IEmpleadoDepartamento | NewEmpleadoDepartamento {
    return form.getRawValue() as IEmpleadoDepartamento | NewEmpleadoDepartamento;
  }

  resetForm(form: EmpleadoDepartamentoFormGroup, empleadoDepartamento: EmpleadoDepartamentoFormGroupInput): void {
    const empleadoDepartamentoRawValue = { ...this.getFormDefaults(), ...empleadoDepartamento };
    form.reset(
      {
        ...empleadoDepartamentoRawValue,
        id: { value: empleadoDepartamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpleadoDepartamentoFormDefaults {
    return {
      id: null,
    };
  }
}
