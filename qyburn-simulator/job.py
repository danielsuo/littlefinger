class Job:
    id = 0

    def __init__(self):
        self.id = Job.id

        Job.id += 1