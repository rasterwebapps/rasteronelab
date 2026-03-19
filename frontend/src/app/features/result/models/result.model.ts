export interface TestResult {
  id: string;
  orderId: string;
  orderLineItemId: string;
  patientId: string;
  testId: string;
  testCode: string;
  testName: string;
  departmentId?: string;
  departmentName?: string;
  sampleId?: string;
  status: ResultStatus;
  enteredBy?: string;
  enteredAt?: string;
  validatedBy?: string;
  validatedAt?: string;
  authorizedBy?: string;
  authorizedAt?: string;
  isCritical: boolean;
  criticalAcknowledged: boolean;
  hasDeltaCheckFailure: boolean;
  isAmended: boolean;
  amendmentReason?: string;
  comments?: string;
  resultValues: ResultValue[];
  createdAt: string;
  updatedAt: string;
}

export type ResultStatus = 'PENDING' | 'ENTERED' | 'VALIDATED' | 'AUTHORIZED' | 'RELEASED' | 'AMENDED';

export interface ResultValue {
  id: string;
  parameterId: string;
  parameterCode: string;
  parameterName: string;
  resultType: string;
  numericValue?: number;
  textValue?: string;
  unit?: string;
  referenceRangeLow?: number;
  referenceRangeHigh?: number;
  referenceRangeText?: string;
  abnormalFlag: string;
  isCritical: boolean;
  previousValue?: number;
  deltaPercentage?: number;
  deltaCheckStatus?: string;
  sortOrder: number;
  isCalculated: boolean;
}

export interface ResultEntryRequest {
  testResultId: string;
  values: ResultValueEntry[];
  comments?: string;
}

export interface ResultValueEntry {
  parameterId: string;
  parameterCode?: string;
  parameterName?: string;
  resultType?: string;
  numericValue?: string;
  textValue?: string;
  unit?: string;
  referenceRangeLow?: string;
  referenceRangeHigh?: string;
  referenceRangeText?: string;
}

export interface ResultAmendRequest {
  testResultId: string;
  amendmentReason: string;
  values?: ResultValueEntry[];
}

export interface TestResultCreateRequest {
  orderId: string;
  orderLineItemId: string;
  patientId: string;
  testId: string;
  testCode?: string;
  testName?: string;
  departmentId?: string;
  departmentName?: string;
  sampleId?: string;
}

export const RESULT_STATUS_COLORS: Record<ResultStatus, string> = {
  PENDING:    'bg-yellow-100 text-yellow-700',
  ENTERED:    'bg-blue-100 text-blue-700',
  VALIDATED:  'bg-green-100 text-green-700',
  AUTHORIZED: 'bg-purple-100 text-purple-700',
  RELEASED:   'bg-teal-100 text-teal-700',
  AMENDED:    'bg-orange-100 text-orange-700',
};
