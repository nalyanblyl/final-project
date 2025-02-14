import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from '../../services/message.service';
import { Message } from '../../models/message.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChannelDetailsComponent } from '../channel-details/channel-details.component';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css'],
  imports: [FormsModule, CommonModule,ChannelDetailsComponent]
})
export class ChatWindowComponent implements OnInit {

  messages: Message[] = [];
  newMessage: string = '';
  currentUserId: number = 1;
  chatType: 'channel' | 'friend' = 'channel';
  targetId: number = 0;

  constructor(private route: ActivatedRoute, private messageService: MessageService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.chatType = params.get('type') as 'channel' | 'friend';
      this.targetId = Number(params.get('id'));

      if (this.chatType === 'channel') {
        this.messageService.getMessagesInChannel(this.targetId).subscribe(data => {
          this.messages = data;
        }, error => {
          console.error('Error fetching channel messages:', error);
        });
      } else if (this.chatType === 'friend') {
        this.messageService.getMessagesWithUser(this.currentUserId, this.targetId).subscribe(data => {
          this.messages = data;
        }, error => {
          console.error('Error fetching private messages:', error);
        });
      }
    });
  }

  sendMessage(): void {
    if (this.newMessage.trim()) {
      const message: Message = {
        id: 0,
        senderId: this.currentUserId,
        recipientId: this.chatType === 'friend' ? this.targetId : undefined,
        channelId: this.chatType === 'channel' ? this.targetId : undefined,
        content: this.newMessage,
        timestamp: new Date(),
        senderNickname: 'JohnDoe'
      };

      if (this.chatType === 'channel') {
        this.messageService.sendMessageToChannel(this.currentUserId, this.targetId, this.newMessage).subscribe(sentMessage => {
          this.messages.push(sentMessage);
          this.newMessage = '';
        });
      } else if (this.chatType === 'friend') {
        this.messageService.sendMessageToUser(this.currentUserId, this.targetId, this.newMessage).subscribe(sentMessage => {
          this.messages.push(sentMessage);
          this.newMessage = '';
        });
      }
    }
  }
}