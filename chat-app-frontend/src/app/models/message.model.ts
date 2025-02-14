export interface Message {
    id: number;
    senderId: number;
    recipientId?: number;
    channelId?: number;
    content: string;
    timestamp: Date;
    senderNickname: string;
  }