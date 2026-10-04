from locust import HttpUser, task, between


class SentinelUser(HttpUser):
    wait_time = between(0.1, 0.2)

    @task
    def login(self):
        self.client.post(
            "/auth/login",
            json={
                "username": "ram",
                "password": "r12"
            }
        )