export interface TestOrder {
  id: string;
  branchId: string;
  orderNumber: string;
  patientId: string;
  visitId?: string;
  referringDoctorId?: string;
  priority: 'ROUTINE' | 'STAT' | 'URGENT';
  status: OrderStatus;
  orderDate: string;
  clinicalHistory?: string;
  specialInstructions?: string;
  barcode?: string;
  estimatedCompletionTime?: string;
  completedAt?: string;
  cancelledAt?: string;
  cancelReason?: string;
  isActive: boolean;
  lineItems: OrderLineItem[];
  createdAt: string;
  updatedAt: string;
}

export type OrderStatus = 'DRAFT' | 'PLACED' | 'PAID' | 'SAMPLE_COLLECTED' | 'IN_PROGRESS' | 'RESULTED' | 'AUTHORISED' | 'COMPLETED' | 'CANCELLED';

export interface OrderLineItem {
  id: string;
  orderId: string;
  testId: string;
  testCode: string;
  testName: string;
  departmentId?: string;
  sampleType?: string;
  tubeType?: string;
  status: string;
  unitPrice: number;
  discountAmount: number;
  netPrice: number;
  isUrgent: boolean;
  isOutsourced: boolean;
  outsourceLab?: string;
  remarks?: string;
}

export interface TestOrderRequest {
  patientId: string;
  visitId?: string;
  referringDoctorId?: string;
  priority?: string;
  clinicalHistory?: string;
  specialInstructions?: string;
  lineItems: OrderLineItemRequest[];
}

export interface OrderLineItemRequest {
  testId: string;
  testCode: string;
  testName: string;
  departmentId?: string;
  sampleType?: string;
  tubeType?: string;
  unitPrice?: number;
  discountAmount?: number;
  isUrgent?: boolean;
  isOutsourced?: boolean;
  outsourceLab?: string;
  remarks?: string;
}
