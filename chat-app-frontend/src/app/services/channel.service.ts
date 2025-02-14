
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Channel } from '../models/channel.model';

export interface Participant {
  userId: number;
  nickname: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChannelService {
  private baseUrl = 'http://localhost:4154/api/channels';

  constructor(private http: HttpClient) {}

  getChannelsForUser(userId: number): Observable<Channel[]> {
    return this.http.get<Channel[]>(`${this.baseUrl}/user/${userId}`);
  }

  createChannel(channelName: string, ownerId: number): Observable<any> {
    const body = {
      channelName: channelName,
      ownerId: ownerId
    };
    return this.http.post<any>(`${this.baseUrl}/create`, body);
  }
  addUserToChannel(channelId: number, userId: number, role: string): Observable<any> {
    const url = `${this.baseUrl}/${channelId}/addUser?userId=${userId}&role=${role}`;
    return this.http.post<any>(url, {});
  }
  
  deleteChannel(channelId: number, requesterId: number): Observable<any> {
    const url = `${this.baseUrl}/${channelId}/delete?requesterId=${requesterId}`;
    return this.http.delete<any>(url);
  }

  getParticipants(channelId: number): Observable<Participant[]> {
    return this.http.get<Participant[]>(`${this.baseUrl}/${channelId}/participants`);
  }

  updateChannelName(channelId: number, requesterId: number, newName: string): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${channelId}/updateName?requesterId=${requesterId}&newName=${newName}`, {});
  }

updateUser(channelId: number, requesterId: number, userId: number, newNickname: string, newRole: string): Observable<any> {
  const url = `${this.baseUrl}/${channelId}/updateUser?requesterId=${requesterId}&userId=${userId}&newNickname=${newNickname}&newRole=${newRole}`;
  return this.http.put<any>(url, {});
}
  
  removeUser(channelId: number, requesterId: number, targetUserId: number): Observable<any> {
    const url = `${this.baseUrl}/${channelId}/removeUser?requesterId=${requesterId}&targetUserId=${targetUserId}`;
    console.log('Sending DELETE request to:', url);
    return this.http.delete(url, { responseType: 'text' });
  }

  getCurrentUserRole(channelId: number): Observable<string> {
    const currentUserId = 1;
    return this.http.get(`${this.baseUrl}/${channelId}/user/${currentUserId}/role`, { responseType: 'text' });
  }
}
