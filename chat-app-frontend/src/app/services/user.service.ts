import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:4154/api/users';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  createUser(user: User): Observable<any> {
    return this.http.post(this.baseUrl, user);
  }
  getFriendsWithNames(userId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/${userId}/friends`);
  }
  addFriend(userId: number, friendId: number): Observable<any> {
    return this.http.post<any>(`http://localhost:4154/api/users/${userId}/addFriend/${friendId}`, {});
  }
  
  removeFriend(userId: number, friendId: number): Observable<any> {
    return this.http.delete<any>(`http://localhost:4154/api/users/${userId}/removeFriend/${friendId}`);
  }
}
