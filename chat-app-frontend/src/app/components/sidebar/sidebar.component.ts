import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Channel } from '../../models/channel.model';
import { User } from '../../models/user.model';
import { CommonModule } from '@angular/common';
import { ChannelService } from '../../services/channel.service';
import { UserService } from '../../services/user.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  standalone: true,
  imports: [CommonModule,RouterModule,FormsModule],
  encapsulation: ViewEncapsulation.None
})

export class SidebarComponent implements OnInit {
  channels: Channel[] = [];
  friends: User[] = [];
  currentUserId: number = 1;
  channelsWithRoles: { channel: Channel, role: string }[] = [];
  channelsWhereFriendIsNotMember: Channel[] = [];
  availableChannels: Channel[] = [];
  selectedFriendId: number | null = null;
  users: User[] = [];

  constructor(private channelService: ChannelService, private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
    this.loadFriends();
    this.channelService.getChannelsForUser(this.currentUserId).subscribe(data => {
      this.channels = data;
    });

  }
  loadFriends(): void {
    this.userService.getFriendsWithNames(this.currentUserId).subscribe((friends) => {
      this.friends = friends;
    });
  }
  loadUsers(): void {
    this.userService.getAllUsers().subscribe((users) => {
      this.users = users.filter(user => user.id !== this.currentUserId);
    });
  }
  isFriend(userId: number): boolean {
    return this.friends.some(friend => friend.id === userId);
  }
  addFriend(user: User): void {
    console.log('Adding friend:', user.username);
    this.userService.addFriend(this.currentUserId, user.id).subscribe({
      next: () => {
        alert(`Friend ${user.username} added successfully.`);
        this.loadFriends();
      },
      error: err => console.error('Error adding friend:', err)
    });
  }
   newChannelName: string = '';

   createNewChannel(): void {
    if (this.newChannelName.trim()) {
      this.channelService.createChannel(this.newChannelName, this.currentUserId).subscribe({
        next: () => {
          alert('Channel created successfully.');
          this.newChannelName = '';
        },
        error: err => console.error('Error creating channel:', err)
      });
    } else {
      alert('Channel name cannot be empty.');
    }
    
  }
  showAvailableChannelsForUser(userId: number): void {
    this.selectedFriendId = userId;
    this.channelService.getChannelsForUser(this.currentUserId).subscribe((myChannels) => {
      this.channelService.getChannelsForUser(userId).subscribe((userChannels) => {
        const userChannelIds = new Set(userChannels.map(channel => channel.id));
        this.availableChannels = myChannels.filter(channel => !userChannelIds.has(channel.id));
      });
    });
  }
  
  addUserToChannel(channelId: number, userId: number): void {
    console.log('Adding user to channel:', channelId);
    this.channelService.addUserToChannel(channelId, userId, 'GUEST').subscribe({
      next: () => {
        alert('User added to channel successfully as GUEST.');
        this.showAvailableChannelsForUser(userId);
      },
      error: err => console.error('Error adding user to channel:', err)
    });
  }
  removeFriend(friend: User): void {
    console.log('Removing friend:', friend.username);
    this.userService.removeFriend(this.currentUserId, friend.id).subscribe({
      next: () => {
        alert(`Friend ${friend.username} removed successfully.`);
        this.friends = this.friends.filter(f => f.id !== friend.id);
      },
      error: err => console.error('Error removing friend:', err)
    });
  }

headerColor = '#FF9800';
sectionHeaderColor = '#000000';

listStyle = {
  'list-style': 'none',
  'padding': '0',
  'margin': '0'
};

listItemStyle = {
  'background-color': '#FFF3E0',
  'padding': '10px',
  'margin': '5px 0',
  'border-radius': '5px',
  'display': 'block',
  'align-items': 'center'
};

linkStyle = {
  'color': '#333',
  'text-decoration': 'none',
  'font-weight': 'bold',
  'cursor': 'pointer'
};

buttonStyle = {
  'background-color': '#FF9800',
  'color': 'white',
  'border': 'none',
  'padding': '5px 10px',
  'border-radius': '5px',
  'cursor': 'pointer',
  'margin-left': '5px'
};

inputStyle = {
  'width': '89%',
  'padding': '10px',
  'border': '1px solid #CCC',
  'border-radius': '5px',
  'margin-bottom': '10px'
};

channelListStyle = {
  'background-color': '#FFFDE7',
  'border': '1px solid #CDDC39',
  'border-radius': '5px',
  'padding': '10px',
  'margin-top': '10px'
};
    
newChannel={
    'border-top': '2px dashed orange',
    'border-bottom': '2px dashed orange',
    'border-left': 'none',
    'border-right': 'none',
    'padding': '20px 0',
    'margin': '20 px 0',
    'background-color': 'transparent'
};
}