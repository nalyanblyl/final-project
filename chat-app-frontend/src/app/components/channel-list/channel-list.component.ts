import { Component, ViewEncapsulation } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChannelService } from '../../services/channel.service';
import { Channel } from '../../models/channel.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-channel-list',
  standalone: true,
  imports: [CommonModule, FormsModule,RouterModule],
  templateUrl: './channel-list.component.html',
  styleUrls: ['./channel-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ChannelListComponent {
  channels: Channel[] = [];
  currentUserId: number = 1;

  constructor(private channelService: ChannelService) {}

  ngOnInit(): void {
    this.channelService.getChannelsForUser(this.currentUserId).subscribe((data: Channel[]) => {
      this.channels = data;
    });
  }
  
}