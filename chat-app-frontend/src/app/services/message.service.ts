import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../models/message.model';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private apiUrl = 'http://localhost:4154/api/messages';  // Базовият път

  constructor(private http: HttpClient) { }

  getMessagesInChannel(channelId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/channel/${channelId}`);
  }
  getMessagesWithUser(senderId: number, recipientId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/private?senderId=${senderId}&recipientId=${recipientId}`);
  }
  sendMessageToChannel(senderId: number, channelId: number, content: string): Observable<Message> {
    const body = {
      senderId: senderId,
      channelId: channelId,
      content: content
    };
    return this.http.post<Message>(`${this.apiUrl}/channel`, body);
  }
  sendMessageToUser(senderId: number, recipientId: number, content: string): Observable<Message> {
    const body = {
      senderId: senderId,
      recipientId: recipientId,
      content: content
    };
    return this.http.post<Message>(`${this.apiUrl}/user`, body);
  }
}