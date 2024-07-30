import api from './Api.js'

/**
 *  json 형태의 데이터가 원칙임 예외시 표기
 */

/**
 *
 * @param data email, password, name
 * @returns {Promise<*>} email, name
 */
export async function signupUser(data) {
    const response = await api.post('/users/signup', data);
    return response.data;
}

/**
 *
 * @param data email, password
 * @returns {Promise<*>} 로그인한 name
 */
export async function loginUser(data) {
    const response = await api.post('/login', data);
    const token  = response.data.token;

    if (token) {
        localStorage.setItem('token', token);
    }

    return response.data.name;
}
