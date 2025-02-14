import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChannelService, Participant } from '../../services/channel.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-channel-details',
  templateUrl: './channel-details.component.html',
  styleUrls: ['./channel-details.component.css'],
  encapsulation: ViewEncapsulation.None,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class ChannelDetailsComponent implements OnInit {
  channelId!: number;
  participants: Participant[] = [];
  editingParticipantId: number | null = null;
  editedNickname: string = '';
  editedRole: string = '';
  isOwnerOrAdmin: boolean = true;
  updatedChannelName: string = '';
  currentUserId: number = 1;

  constructor(private route: ActivatedRoute, private channelService: ChannelService) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
    this.channelId = +params['id'];
    this.loadParticipants();
    this.checkUserRole();
    });
  }

  loadParticipants(): void {
    this.channelService.getParticipants(this.channelId).subscribe((participants) => {
      this.participants = participants;
    });
  }

  checkUserRole(): void {
    this.channelService.getCurrentUserRole(this.channelId).subscribe((role) => {
      this.isOwnerOrAdmin = role === 'OWNER' || role === 'ADMIN';
      console.log('Is user Owner or Admin:', this.isOwnerOrAdmin);
    });
  }

  editParticipant(participant: Participant): void {
    this.editingParticipantId = participant.userId;
    this.editedNickname = participant.nickname;
    this.editedRole = participant.role;
  }

  saveUserChanges(participant: Participant): void {
    const requesterId = this.currentUserId;
    console.log('Updating user:', {
      requesterId,
      userId: participant.userId,
      newNickname: this.editedNickname,
      newRole: this.editedRole
    });

    this.channelService.updateUser(
      this.channelId,
      requesterId,
      participant.userId,
      this.editedNickname,
      this.editedRole
    ).subscribe({
      next: () => {
        participant.nickname = this.editedNickname;
        participant.role = this.editedRole;
        this.cancelEditing();
        alert('User updated successfully.');
      },
      error: err => {
        console.error('Error updating user:', err);
        this.cancelEditing();
      }
    });
  }

  cancelEditing(): void {
    this.editingParticipantId = null;
    this.editedNickname = '';
    this.editedRole = '';
  }

  updateChannelName(): void {
    if (this.updatedChannelName.trim()) {
      this.channelService.updateChannelName(this.channelId, this.currentUserId, this.updatedChannelName).subscribe({
        next: () => {
          alert('Channel name updated successfully!');
          this.updatedChannelName = '';
        },
        error: err => console.error('Error updating channel name:', err)
      });
    }
  }

  removeUser(participant: Participant): void {
    const requesterId = this.currentUserId;
    const targetUserId = participant.userId;

    this.channelService.removeUser(this.channelId, requesterId, targetUserId).subscribe({
        next: (response) => {
            console.log(response); // Очаква се текст: "User removed successfully."
            this.participants = this.participants.filter(p => p.userId !== targetUserId);
            alert(`User ${participant.nickname} has been removed.`);
        },
        error: (err) => {
            console.error('Error removing user:', err);
            alert(`Failed to remove user: ${err.message}`);
        }
    });
  }
  deleteChannel(): void {
    const requesterId = this.currentUserId;
    console.log('Deleting channel:', this.channelId);

    this.channelService.deleteChannel(this.channelId, requesterId).subscribe({
      next: () => {
        alert('Channel deleted successfully.');
      },
      error: err => console.error('Error deleting channel:', err)
    });
  }
headerColor = '#FF9800';
participantBgColor = '#FFF3E0';
buttonStyle = {
  'background-color': '#FF9800',
  'color': 'white',
  'border': 'none',
  'padding': '5px 10px',
  'margin': '5px',
  'border-radius': '5px',
  'cursor': 'pointer'
};

inputStyle = {
  'border': '1px solid #CCC',
  'padding': '5px',
  'border-radius': '5px',
  'width': '100%'
};

deleteButtonStyle = {
  'background-color': '#E53935',
  'color': 'white',
  'border': 'none',
  'padding': '5px 10px',
  'border-radius': '5px',
  'cursor': 'pointer',
};
editForm={
    'border-top': '2px dashed orange',
    'border-bottom': '2px dashed orange',
    'border-left': 'none',
    'border-right': 'none',
    'padding': '20px 0',
    'margin': '20 px 0',
    'background-color': 'transparent'
};
}
