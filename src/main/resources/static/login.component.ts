import { Component } from '@angular/core';
import { ApiService } from './api.service'; // Import your ApiService

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    constructor(private api: ApiService) { }

    signIn() {
        // Your authentication logic...
        // After successful authentication response:
        const response = {
            data: {
                accessToken: 'your-access-token',
                refreshToken: 'your-refresh-token'
            }
        }; // Replace with actual authentication response

        this.api.saveAccessToken(response.data.accessToken);
        this.api.saveRefreshToken(response.data.refreshToken);
        // Other logic after signing in...
    }
}
