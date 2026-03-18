// Pagination
export interface PageRequest {
  page: number;
  size: number;
  sort?: string;
  direction?: 'asc' | 'desc';
}

export interface PageResponse<T> {
  data: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

// LIS-029: Branch
export interface Branch {
  id: string;
  code: string;
  name: string;
  type: 'MAIN_LAB' | 'SATELLITE' | 'COLLECTION_CENTER' | 'FRANCHISE';
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  country: string;
  postalCode: string;
  phone: string;
  email: string;
  latitude?: number;
  longitude?: number;
  licenseNumber?: string;
  nablNumber?: string;
  managerUserId?: string;
  parentBranchId?: string;
  isActive: boolean;
  organizationId: string;
  createdAt: string;
  updatedAt: string;
}

export interface WorkingHours {
  id: string;
  branchId: string;
  dayOfWeek: string;
  openTime: string;
  closeTime: string;
  isClosed: boolean;
}

// LIS-030: Test Master
export interface TestMaster {
  id: string;
  testCode: string;
  testName: string;
  shortName?: string;
  departmentId: string;
  departmentName?: string;
  sampleTypeId: string;
  sampleTypeName?: string;
  tubeTypeId?: string;
  tubeTypeName?: string;
  sampleVolumeMl?: number;
  testType: 'SINGLE' | 'PANEL' | 'PROFILE' | 'CALCULATED';
  method?: string;
  tatHours?: number;
  isOutsourced: boolean;
  partnerLabId?: string;
  loincCode?: string;
  cptCode?: string;
  reportTemplateId?: string;
  displayOrder: number;
  patientInstructions?: string;
  isActive: boolean;
}

// LIS-030: Parameter Master
export interface ParameterMaster {
  id: string;
  paramCode: string;
  paramName: string;
  shortName?: string;
  testId: string;
  testName?: string;
  dataType: 'NUMERIC' | 'TEXT' | 'OPTION' | 'FORMULA' | 'SEMI_QUANTITATIVE';
  unitCode?: string;
  loincCode?: string;
  decimalPlaces?: number;
  resultOptions?: string[];
  formula?: string;
  displayOrder: number;
  isCritical: boolean;
  isReportable: boolean;
  isDerived: boolean;
  isActive: boolean;
}

// LIS-030: Reference Range
export interface ReferenceRange {
  id: string;
  parameterId: string;
  parameterName?: string;
  gender: 'MALE' | 'FEMALE' | 'ALL';
  ageMinDays: number;
  ageMaxDays: number;
  trimester?: 'T1' | 'T2' | 'T3' | null;
  normalLow?: number;
  normalHigh?: number;
  criticalLow?: number;
  criticalHigh?: number;
  panicLow?: number;
  panicHigh?: number;
  unitCode?: string;
  interpretation?: string;
}

// LIS-030: Test Panel
export interface TestPanel {
  id: string;
  panelCode: string;
  panelName: string;
  departmentId: string;
  departmentName?: string;
  testIds: string[];
  tests?: TestMaster[];
  displayOrder: number;
  isActive: boolean;
}

// LIS-031: Doctor
export interface Doctor {
  id: string;
  name: string;
  specialization?: string;
  phone?: string;
  email?: string;
  registrationNumber?: string;
  referralCommission?: number;
  branchIds: string[];
  isActive: boolean;
}

// LIS-031: User
export interface User {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  employeeId?: string;
  department?: string;
  designation?: string;
  role: string;
  branchIds: string[];
  isActive: boolean;
}

// LIS-031: Role
export interface Role {
  id: string;
  roleName: string;
  description?: string;
  permissions: Permission[];
  isActive: boolean;
}

export interface Permission {
  feature: string;
  actions: string[];
}

// LIS-032: Holiday
export interface Holiday {
  id: string;
  date: string;
  name: string;
  holidayType: 'NATIONAL' | 'ORGANIZATIONAL' | 'REGIONAL';
  branchIds: string[];
}

// LIS-032: Notification Template
export interface NotificationTemplate {
  id: string;
  templateCode: string;
  templateName: string;
  channel: 'SMS' | 'EMAIL' | 'WHATSAPP';
  templateBody: string;
  eventTrigger: string;
  isActive: boolean;
}

// LIS-032: Report Template
export interface ReportTemplate {
  id: string;
  departmentId: string;
  departmentName?: string;
  templateName: string;
  description?: string;
  headerConfig?: string;
  footerConfig?: string;
  logoUrl?: string;
  fontFamily?: string;
  layoutType: 'PORTRAIT' | 'LANDSCAPE';
  isActive: boolean;
}

// LIS-032: Number Series
export interface NumberSeries {
  id: string;
  entityType: string;
  prefix: string;
  format: string;
  nextSequence: number;
  branchId: string;
}

// LIS-032: Discount Scheme
export interface DiscountScheme {
  id: string;
  schemeCode: string;
  schemeName: string;
  applicableTo: 'WALK_IN' | 'CORPORATE' | 'INSURANCE';
  discountType: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  minTransactionAmount?: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
}

// LIS-032: Insurance Tariff
export interface InsuranceTariff {
  id: string;
  insuranceName: string;
  planName: string;
  testId: string;
  testName?: string;
  tariffRate: number;
  effectiveFrom: string;
  effectiveTo?: string;
  isActive: boolean;
}

// LIS-032: Antibiotic
export interface Antibiotic {
  id: string;
  antibioticCode: string;
  antibioticName: string;
  group: string;
  clsiBreakpointS?: number;
  clsiBreakpointI?: number;
  clsiBreakpointR?: number;
  isActive: boolean;
}

// LIS-032: Microorganism
export interface Microorganism {
  id: string;
  organismCode: string;
  organismName: string;
  gramType: 'GRAM_POSITIVE' | 'GRAM_NEGATIVE' | 'FUNGAL' | 'ACID_FAST';
  clinicalSignificance?: string;
  isActive: boolean;
}

// LIS-032: Critical Value
export interface CriticalValue {
  id: string;
  parameterId: string;
  parameterName?: string;
  criticalLow?: number;
  criticalHigh?: number;
  alertAction: 'EMAIL' | 'SMS' | 'CALL';
  contactList: string;
  alertPriority: 'LOW' | 'MEDIUM' | 'HIGH';
  isActive: boolean;
}

// LIS-032: Delta Check
export interface DeltaCheck {
  id: string;
  parameterId: string;
  parameterName?: string;
  percentageThreshold: number;
  timeWindowDays: number;
  flagAction: 'WARN' | 'BLOCK' | 'REQUIRE_APPROVAL';
  isActive: boolean;
}

// LIS-032: Auto-Validation Rule
export interface AutoValidationRule {
  id: string;
  ruleCode: string;
  ruleName: string;
  conditions: string;
  departmentIds: string[];
  rulePriority: number;
  isActive: boolean;
}

// LIS-032: TAT Configuration
export interface TatConfiguration {
  id: string;
  testId: string;
  testName?: string;
  routineTatHours: number;
  statTatHours: number;
  emergencyTatHours: number;
  isActive: boolean;
}

// Department (shared reference)
export interface Department {
  id: string;
  departmentCode: string;
  departmentName: string;
}
