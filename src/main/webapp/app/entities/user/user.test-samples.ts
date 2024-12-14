import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 12238,
  login: 'JmhXlx',
};

export const sampleWithPartialData: IUser = {
  id: 14253,
  login: 'TJcv',
};

export const sampleWithFullData: IUser = {
  id: 14604,
  login: 'cXDjz9@e2jDdq\\C6\\>kf\\+Qu8mNO\\?QUg\\Ru5PC',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
