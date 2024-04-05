//from sir freds code

// export class ApiService {
//
//     async refreshToken() {
//         return axios.post("/auth/refreshtoken", {
//             refreshToken: this.getRefreshToken(),
//         });
//     }
//
//     getRefreshToken() : string {
//         const token = localStorage.getItem("refreshToken");
//
//         if (token) {
//             return token;
//         }
//
//         return "";
//     }
//
//     saveRefreshToken(accessToken: string) {
//         localStorage.setItem("refreshToken", accessToken);
//     }
//
//     removeRefreshToken() {
//         localStorage.removeItem("refreshToken");
//     }
//}
// api.service.ts

// this is fixed and working
// import { Injectable } from '@angular/core';
// import axios from 'axios';
//
// @Injectable({
//     providedIn: 'root'
// })
// export class ApiService {
//     refreshRetry: boolean = false;
//
//     constructor() { }
//
//     async refreshToken() {
//         return axios.post("/auth/refreshtoken", {
//             refreshToken: this.getRefreshToken(),
//         });
//     }
//
//     getRefreshToken(): string {
//         const token = localStorage.getItem("refreshToken");
//         if (token) {
//             return token;
//         }
//         return "";
//     }
//
//     saveRefreshToken(accessToken: string) {
//         localStorage.setItem("refreshToken", accessToken);
//     }
//
//     removeRefreshToken() {
//         localStorage.removeItem("refreshToken");
//     }
// }

import { Injectable } from '@angular/core';
import axios, { AxiosError } from 'axios';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    refreshRetry: boolean = false;

    constructor() {
        this.setupInterceptor(); // Call the interceptor setup method when the service is instantiated
    }

    async refreshToken() {
        return axios.post("/auth/refreshtoken", {
            refreshToken: this.getRefreshToken(),
        });
    }
    saveRefreshToken(refreshToken: string) {
        localStorage.setItem("refreshToken", refreshToken);
    }

    getRefreshToken(): string {
        const token = localStorage.getItem("refreshToken");
        if (token) {
            return token;
        }
        return "";
    }

    saveAccessToken(accessToken: string) {
        this.refreshRetry = false; // Reset the refreshRetry variable
        localStorage.setItem("accessToken", accessToken);
    }

    removeRefreshToken() {
        localStorage.removeItem("refreshToken");
    }

    setupInterceptor() {
        axios.interceptors.response.use(
            (res) => {
                return res;
            },
            async (err: AxiosError) => { // Specify AxiosError type for err parameter
                const originalConfig = err.config;

                if (err.response) {
                    // Access Token was expired
                    if (err.response.status === 401 && !this.refreshRetry && originalConfig.url !== '/auth/login') {
                        console.log("Attempting token refresh")
                        this.refreshRetry = true;

                        try {
                            const response = await this.refreshToken();

                            console.log("Token refresh response")
                            console.log(response)

                            this.saveAccessToken(response.data.accessToken)

                            originalConfig.headers["Authorization"] = "Bearer " + response.data.accessToken;

                            return axios(originalConfig);
                        } catch (error) {
                            console.log("error")
                            console.log(error)
                            return Promise.reject(error);
                        }
                    }
                }

                return Promise.reject(err);
            }
        );
    }
}
