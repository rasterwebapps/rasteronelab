export type ReportStatus = 'GENERATED' | 'SIGNED' | 'DELIVERED' | 'AMENDED' | 'CANCELLED';

export interface LabReport {
  id: string;
  orderId: string;
  patientId: string;
  patientName: string;
  reportNumber: string;
  reportType: string;
  reportStatus: ReportStatus;
  departmentId?: string;
  departmentName?: string;
  generatedBy?: string;
  generatedAt?: string;
  signedBy?: string;
  signedAt?: string;
  deliveredAt?: string;
  deliveryChannel?: string;
  fileUrl?: string;
  fileName?: string;
  fileSize?: number;
  pageCount?: number;
  templateId?: string;
  version?: number;
  amendmentReason?: string;
  notes?: string;
  branchId: string;
  createdAt: string;
  updatedAt: string;
}

export interface ReportGenerateRequest {
  orderId: string;
  patientId: string;
  patientName: string;
  departmentId?: string;
  departmentName?: string;
  reportType?: string;
  templateId?: string;
  notes?: string;
}

export interface ReportSignRequest {
  signedBy: string;
  notes?: string;
}

export interface ReportDeliverRequest {
  deliveryChannel: string;
  recipientEmail?: string;
  recipientPhone?: string;
}

export const REPORT_STATUS_COLORS: Record<ReportStatus, string> = {
  GENERATED: 'bg-blue-100 text-blue-700',
  SIGNED:    'bg-purple-100 text-purple-700',
  DELIVERED: 'bg-green-100 text-green-700',
  AMENDED:   'bg-orange-100 text-orange-700',
  CANCELLED: 'bg-red-100 text-red-700',
};
