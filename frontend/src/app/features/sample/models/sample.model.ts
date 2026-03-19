export type SampleStatus =
  | 'COLLECTED'
  | 'RECEIVED'
  | 'ACCEPTED'
  | 'REJECTED'
  | 'ALIQUOTED'
  | 'PROCESSING'
  | 'COMPLETED'
  | 'STORED'
  | 'DISPOSED';

export type TubeType = 'RED' | 'EDTA' | 'CITRATE' | 'FLUORIDE' | 'HEPARIN' | 'SST' | 'OTHER';

export type RejectionReason =
  | 'HEMOLYZED'
  | 'CLOTTED'
  | 'INSUFFICIENT'
  | 'WRONG_TUBE'
  | 'UNLABELED'
  | 'LIPEMIC'
  | 'ICTERIC'
  | 'DETERIORATED'
  | 'OLD';

export interface Sample {
  id: string;
  orderId: string;
  patientId: string;
  sampleBarcode: string;
  tubeType: TubeType;
  sampleType?: string;
  status: SampleStatus;
  collectedBy: string;
  collectedAt: string;
  collectionSite?: string;
  quantity?: number;
  unit?: string;
  homeCollection: boolean;
  notes?: string;
  receivedBy?: string;
  receivedAt?: string;
  tatStartedAt?: string;
  temperature?: number;
  rejectionReason?: RejectionReason;
  rejectionComment?: string;
  recollectionRequired?: boolean;
  rejectedBy?: string;
  rejectedAt?: string;
  parentSampleId?: string;
  aliquotLabel?: string;
  storageRack?: string;
  storageShelf?: string;
  storagePosition?: string;
  storedAt?: string;
  disposalDate?: string;
  departmentId?: string;
  branchId: string;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
}

export interface SampleTubeRequest {
  tubeType: TubeType;
  sampleType?: string;
  collectionSite?: string;
  collectedBy: string;
  collectedAt: string;
  quantity?: number;
  unit?: string;
}

export interface SampleCollectRequest {
  orderId: string;
  patientId: string;
  tubes: SampleTubeRequest[];
  homeCollection?: boolean;
  notes?: string;
}

export interface SampleReceiveRequest {
  receivedBy: string;
  receivedAt: string;
  temperature?: number;
  notes?: string;
}

export interface SampleRejectRequest {
  rejectedBy: string;
  rejectionReason: RejectionReason;
  comments?: string;
  recollectionRequired?: boolean;
}

export interface AliquotRequest {
  departmentId: string;
  volume?: number;
  unit?: string;
}

export interface SampleAliquotRequest {
  aliquots: AliquotRequest[];
}

export interface SampleStorageRequest {
  storageRack: string;
  storageShelf: string;
  storagePosition: string;
  disposalDate?: string;
}

export interface SampleTransferRequest {
  destinationBranchId: string;
  transferredBy: string;
  reason?: string;
  notes?: string;
}

export interface SampleTrackingEntry {
  id: string;
  sampleId: string;
  status: SampleStatus;
  eventTime: string;
  performedBy?: string;
  comments?: string;
}

export const TUBE_TYPE_COLORS: Record<TubeType, string> = {
  RED: '#ef4444',
  EDTA: '#8b5cf6',
  CITRATE: '#3b82f6',
  FLUORIDE: '#6b7280',
  HEPARIN: '#22c55e',
  SST: '#f59e0b',
  OTHER: '#94a3b8',
};

export const STATUS_COLORS: Record<SampleStatus, string> = {
  COLLECTED:  'bg-blue-100 text-blue-700',
  RECEIVED:   'bg-cyan-100 text-cyan-700',
  ACCEPTED:   'bg-green-100 text-green-700',
  REJECTED:   'bg-red-100 text-red-700',
  ALIQUOTED:  'bg-purple-100 text-purple-700',
  PROCESSING: 'bg-yellow-100 text-yellow-700',
  COMPLETED:  'bg-emerald-100 text-emerald-700',
  STORED:     'bg-gray-100 text-gray-700',
  DISPOSED:   'bg-slate-100 text-slate-500',
};
