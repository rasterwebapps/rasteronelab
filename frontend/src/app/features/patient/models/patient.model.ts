export interface Patient {
  id: string;
  uhid: string;
  branchId: string;
  firstName: string;
  lastName?: string;
  dateOfBirth?: string;
  ageYears?: number;
  ageMonths?: number;
  ageDays?: number;
  gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN';
  phone: string;
  email?: string;
  bloodGroup?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  nationality?: string;
  idType?: string;
  idNumber?: string;
  notes?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface PatientRequest {
  firstName: string;
  lastName?: string;
  dateOfBirth?: string;
  ageYears?: number;
  ageMonths?: number;
  ageDays?: number;
  gender: string;
  phone: string;
  email?: string;
  bloodGroup?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  nationality?: string;
  idType?: string;
  idNumber?: string;
  notes?: string;
}

export interface PatientVisit {
  id: string;
  patientId: string;
  visitNumber: string;
  visitDate: string;
  visitType: 'WALK_IN' | 'APPOINTMENT' | 'HOME_COLLECTION' | 'CAMP' | 'REFERRAL';
  referringDoctorId?: string;
  clinicalNotes?: string;
  isActive: boolean;
}
