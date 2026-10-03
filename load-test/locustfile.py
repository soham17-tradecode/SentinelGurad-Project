from locust import HttpUser, task, between


class SentinelUser(HttpUser):
    wait_time = between(1, 2)

    @task
    def test_gateway(self):
        self.client.get("/auth")