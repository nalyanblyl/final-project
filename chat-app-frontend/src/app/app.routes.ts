import { Routes } from '@angular/router';
import { ChatWindowComponent } from './components/chat-window/chat-window.component';

export const routes: Routes = [

  { path: 'api/:type/:id', component: ChatWindowComponent },  // Добавен маршрут за ChatWindowComponent
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/home' }
];