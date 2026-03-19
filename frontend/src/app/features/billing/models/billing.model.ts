export type InvoiceStatus = 'DRAFT' | 'GENERATED' | 'PARTIALLY_PAID' | 'PAID' | 'REFUNDED' | 'CANCELLED';
export type PaymentMethod = 'CASH' | 'CARD' | 'UPI' | 'INSURANCE' | 'CREDIT' | 'ONLINE';
export type RateListType = 'WALK_IN' | 'CORPORATE' | 'INSURANCE' | 'DOCTOR_REF';

export interface Invoice {
  id: string;
  branchId: string;
  invoiceNumber: string;
  orderId: string;
  patientId: string;
  rateListType: RateListType;
  subtotal: number;
  discountAmount: number;
  discountType?: string;
  discountReason?: string;
  taxAmount: number;
  totalAmount: number;
  paidAmount: number;
  balanceAmount: number;
  status: InvoiceStatus;
  invoiceDate: string;
  dueDate?: string;
  notes?: string;
  isActive: boolean;
  lineItems: InvoiceLineItem[];
  createdAt: string;
  updatedAt: string;
}

export interface InvoiceLineItem {
  id: string;
  invoiceId: string;
  orderLineItemId?: string;
  testId: string;
  testCode: string;
  testName: string;
  quantity: number;
  unitPrice: number;
  discountAmount: number;
  netAmount: number;
}

export interface InvoiceRequest {
  orderId: string;
  patientId: string;
  rateListType?: string;
  discountType?: string;
  discountAmount?: number;
  discountReason?: string;
  notes?: string;
  lineItems: InvoiceLineItemRequest[];
}

export interface InvoiceLineItemRequest {
  orderLineItemId?: string;
  testId: string;
  testCode: string;
  testName: string;
  quantity?: number;
  unitPrice: number;
  discountAmount?: number;
}

export interface Payment {
  id: string;
  branchId: string;
  invoiceId: string;
  invoiceNumber?: string;
  receiptNumber: string;
  amount: number;
  paymentMethod: PaymentMethod;
  transactionRef?: string;
  paymentDate: string;
  receivedBy?: string;
  notes?: string;
  isActive: boolean;
  createdAt: string;
}

export interface PaymentRequest {
  invoiceId: string;
  amount: number;
  paymentMethod: string;
  transactionRef?: string;
  notes?: string;
}
