// 공통 API 요청 함수
async function apiRequest(path, method, body) {
    try {
        const res = await fetch(path, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include', // 쿠키(accessToken, refreshToken) 사용
            body: body ? JSON.stringify(body) : null
        });

        const text = await res.text();
        let json = null;

        try {
            json = text ? JSON.parse(text) : null;
        } catch (e) {
        }

        return { ok: res.ok, status: res.status, body: json, text: text };
    } catch (e) {
        return { ok: false, status: 0, body: null, text: e.message };
    }
}

function setMessage(elementId, ok, msg) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.textContent = msg;
    el.classList.remove('success', 'error');
    el.classList.add(ok ? 'success' : 'error');
}

// 페이지 로드 시 폼 이벤트 바인딩
document.addEventListener('DOMContentLoaded', function () {
    if (window.AUTH_PAGE === 'SIGNUP') {
        bindSignUpForm();
    } else if (window.AUTH_PAGE === 'LOGIN') {
        bindLoginForm();
    }
});

function bindSignUpForm() {
    const form = document.getElementById('signup-form');
    if (!form) return;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const studentNumber = document.getElementById('signup-studentNumber').value.trim();
        const password = document.getElementById('signup-password').value.trim();
        const name = document.getElementById('signup-name').value.trim();
        const department = document.getElementById('signup-department').value.trim();
        const gradeStr = document.getElementById('signup-grade').value.trim();

        if (!studentNumber || !password || !name) {
            setMessage('signup-message', false, '학번, 비밀번호, 이름은 필수입니다.');
            return;
        }

        const grade = gradeStr ? parseInt(gradeStr, 10) : null;

        const body = {
            studentNumber: studentNumber,
            password: password,
            name: name,
            department: department || null,
            grade: grade
        };

        const res = await apiRequest('/api/auth/signup', 'POST', body);

        if (res.body && typeof res.body.status === 'number') {
            const ok = res.body.status >= 200 && res.body.status < 300;
            setMessage('signup-message', ok, res.body.message || '회원가입 응답 수신');
        } else {
            setMessage(
                'signup-message',
                res.ok,
                res.text || ('회원가입 요청 결과 코드: ' + res.status)
            );
        }
    });
}

function bindLoginForm() {
    const form = document.getElementById('login-form');
    if (!form) return;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const studentNumber = document.getElementById('login-studentNumber').value.trim();
        const password = document.getElementById('login-password').value.trim();

        if (!studentNumber || !password) {
            setMessage('login-message', false, '학번과 비밀번호를 입력하세요.');
            return;
        }

        const body = {
            studentNumber: studentNumber,
            password: password
        };

        const res = await apiRequest('/api/auth/login', 'POST', body);

        if (res.body && typeof res.body.status === 'number') {
            const ok = res.body.status >= 200 && res.body.status < 300;
            let msg = res.body.message || (ok ? '로그인 성공' : '로그인 실패');

            if (res.body.data && res.body.data.accessToken) {
                msg += ' (accessToken 수신)';
            }

            setMessage('login-message', ok, msg);
        } else {
            setMessage(
                'login-message',
                res.ok,
                res.text || ('로그인 요청 결과 코드: ' + res.status)
            );
        }
    });
}
